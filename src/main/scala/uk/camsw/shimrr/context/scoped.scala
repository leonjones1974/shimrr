package uk.camsw.shimrr.context

import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist
import shapeless.ops.record.Selector
import shapeless.{:+:, ::, =:!=, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric, Lazy}
import uk.camsw.shimrr.Migration

object scoped {

  trait Scope[A]

  private[shimrr] trait MigrationContext[A, FieldDefaults <: HList] extends Scope[A] {
    type OUT = FieldDefaults
    val fieldDefaults: FieldDefaults
  }


  object MigrationContext {

    class ScopedBuilder[A] {
      def apply[FieldDefaults <: HList](defaults: FieldDefaults = HNil): MigrationContext[A, FieldDefaults] = new MigrationContext[A, FieldDefaults] {
        override val fieldDefaults: FieldDefaults = defaults
      }
    }

    def apply[A] = new ScopedBuilder[A]
  }


  implicit def cNilMigration[T <: CNil, B, A, FieldDefaults <: HList]: Migration[T, B] =
    Migration.instance(a =>
      throw new RuntimeException(s"Will not happen, but did for $a"))


  implicit def coproductMigration[A, B, ARepr <: Coproduct](
                                                             implicit
                                                             genA: Generic.Aux[A, ARepr],
                                                             m: Migration[ARepr, B]): Migration[A, B] =
    Migration.instance(a =>
      m.migrate(genA.to(a))
    )


  implicit def coproductReprMigration[H, T <: Coproduct, B, BRepr <: HList, A, FieldDefaults <: HList](
                                                                                                         implicit
                                                                                                         mH: Lazy[Migration[H, B]],
                                                                                                         mT: Migration[T, B]
                                                                                                       ): Migration[H :+: T, B] =
    Migration.instance {
      case Inl(h) =>
        mH.value.migrate(h)
      case Inr(t) =>
        mT.migrate(t)
    }


  implicit def productMigration[
  A, ARepr <: HList,
  B, BRepr <: HList,
  Common <: HList,
  Added <: HList,
  Unaligned <: HList,
  FieldDefaults <: HList](
                            implicit
                            scope: Scope[A],
                            genA: LabelledGeneric.Aux[A, ARepr],
                            genB: LabelledGeneric.Aux[B, BRepr],
                            inter: hlist.Intersection.Aux[ARepr, BRepr, Common],
                            diff: hlist.Diff.Aux[BRepr, Common, Added],
                            defaulter: ScopedDefaulter[A, Added],
                            prepend: hlist.Prepend.Aux[Added, Common, Unaligned],
                            align: hlist.Align[Unaligned, BRepr]
                          ): Migration[A, B] =
    Migration.instance {
      a =>
        genB.from(align(prepend(defaulter.defaultFor(a), inter(genA.to(a)))))

    }

  implicit def polyProductMigration[
  S,
  A, ARepr <: HList,
  B, BRepr <: HList,
  Common <: HList,
  Added <: HList,
  Unaligned <: HList,
  FieldDefaults <: HList](
                            implicit
                            scope: Scope[S],
                            scopeEv: A <:< S,
                            notEq: A =:!= S,
                            genA: LabelledGeneric.Aux[A, ARepr],
                            genB: LabelledGeneric.Aux[B, BRepr],
                            inter: hlist.Intersection.Aux[ARepr, BRepr, Common],
                            diff: hlist.Diff.Aux[BRepr, Common, Added],
                            defaulter: ScopedDefaulter[S, Added],
                            prepend: hlist.Prepend.Aux[Added, Common, Unaligned],
                            align: hlist.Align[Unaligned, BRepr]
                          ): Migration[A, B] =
    Migration.instance {
      a =>
        genB.from(align(prepend(defaulter.defaultFor(a), inter(genA.to(a)))))

    }

  implicit def literalFieldDefaulter[A, FieldDefaults <: HList, K <: Symbol, H, T <: HList](
                                                                                              implicit
                                                                                              scope: Scope[A],
                                                                                              ctx: MigrationContext[A, FieldDefaults],
                                                                                              selector: Selector.Aux[FieldDefaults, K, H],
                                                                                              dT: ScopedDefaulter[A, T]
                                                                                            ): ScopedDefaulter[A, FieldType[K, H] :: T] =
    ScopedDefaulter.instance[A] { a =>
      field[K](selector(ctx.fieldDefaults)) :: field[K](dT.defaultFor(a))
    }

  implicit def lazyLiteralFieldDefaulter[A, FieldDefaults <: HList, K <: Symbol, H, T <: HList](
                                                                                                  implicit
                                                                                                  scope: Scope[A],
                                                                                                  ctx: MigrationContext[A, FieldDefaults],
                                                                                                  selector: Selector.Aux[FieldDefaults, K, () => H],
                                                                                                  dT: ScopedDefaulter[A, T]
                                                                                                ): ScopedDefaulter[A, FieldType[K, H] :: T] =
    ScopedDefaulter.instance[A] { a =>
      field[K](selector(ctx.fieldDefaults)()) :: field[K](dT.defaultFor(a))
    }

  implicit def parameterizedLazyFieldDefaulter[A, FieldDefaults <: HList, K <: Symbol, H, T <: HList](
                                                                                                        implicit
                                                                                                        scope: Scope[A],
                                                                                                        ctx: MigrationContext[A, FieldDefaults],
                                                                                                        selector: Selector.Aux[FieldDefaults, K, (A) => H],
                                                                                                        dT: ScopedDefaulter[A, T]
                                                                                                      ): ScopedDefaulter[A, FieldType[K, H] :: T] = {
    ScopedDefaulter.instance[A] { a =>
      field[K](selector(ctx.fieldDefaults)(a)) :: field[K](dT.defaultFor(a))
    }
  }


  implicit def hNilDefaulter[A, FieldDefaults <: HList]: ScopedDefaulter[A, HNil] =
    ScopedDefaulter.instance(_ => HNil)
}
