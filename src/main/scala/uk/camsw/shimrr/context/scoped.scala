package uk.camsw.shimrr.context

import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist
import shapeless.ops.record.Selector
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric}
import uk.camsw.shimrr.Migration

object scoped {

  trait Scope[C, A]

  private[shimrr] trait MigrationContext[C, A, FIELD_DEFAULTS <: HList] extends Scope[C, A] {
    type OUT = FIELD_DEFAULTS
    val fieldDefaults: FIELD_DEFAULTS
  }


  object MigrationContext {

    class ScopedBuilder[C, A] {
      def apply[FIELD_DEFAULTS <: HList](defaults: FIELD_DEFAULTS = HNil): MigrationContext[C, A, FIELD_DEFAULTS] = new MigrationContext[C, A, FIELD_DEFAULTS] {
        override val fieldDefaults: FIELD_DEFAULTS = defaults
      }
    }

    def apply[C, A] = new ScopedBuilder[C, A]
  }


  implicit def cNilMigration[T <: CNil, B, A, FIELD_DEFAULTS <: HList]: Migration[T, B] =
    Migration.instance(a =>
      throw new RuntimeException(s"Will not happen, but did for $a"))


  implicit def coproductMigration[A, B, ARepr <: Coproduct](implicit
                                                            genA: Generic.Aux[A, ARepr],
                                                            m: Migration[ARepr, B]): Migration[A, B] =
    Migration.instance(a =>
      m.migrate(genA.to(a))
    )


  implicit def coproductReprMigration[H, T <: Coproduct, B, BRepr <: HList, A, FIELD_DEFAULTS <: HList](implicit
                                                                                                        mH: Migration[H, B],
                                                                                                        mT: Migration[T, B]
                                                                                                       ): Migration[H :+: T, B] =
    Migration.instance {
      case Inl(h) =>
        mH.migrate(h)
      case Inr(t) =>
        mT.migrate(t)
    }


  implicit def productMigration[
  C,
  A, ARepr <: HList,
  B, BRepr <: HList,
  Common <: HList,
  Added <: HList,
  Unaligned <: HList,
  FIELD_DEFAULTS <: HList](
                            implicit
                            scope: Scope[C, A],
                            genA: LabelledGeneric.Aux[A, ARepr],
                            genB: LabelledGeneric.Aux[B, BRepr],
                            inter: hlist.Intersection.Aux[ARepr, BRepr, Common],
                            diff: hlist.Diff.Aux[BRepr, Common, Added],
                            defaulter: ScopedDefaulter[C, A, Added],
                            prepend: hlist.Prepend.Aux[Added, Common, Unaligned],
                            align: hlist.Align[Unaligned, BRepr]
                          ): Migration[A, B] =
    Migration.instance {
      a =>
        genB.from(align(prepend(defaulter.defaultFor(a), inter(genA.to(a)))))

    }

  implicit def literalFieldDefaulter[C, A, FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                                 implicit
                                                                                                 scope: Scope[C, A],
                                                                                                 ctx: MigrationContext[C, A, FIELD_DEFAULTS],
                                                                                                 selector: Selector.Aux[FIELD_DEFAULTS, K, H],
                                                                                                 dT: ScopedDefaulter[C, A, T]
                                                                                               ): ScopedDefaulter[C, A, FieldType[K, H] :: T] =
    ScopedDefaulter.instance[C, A] { a =>
      field[K](selector(ctx.fieldDefaults)) :: field[K](dT.defaultFor(a))
    }

  implicit def lazyLiteralFieldDefaulter[C, A, FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                                     implicit
                                                                                                     scope: Scope[C, A],
                                                                                                     ctx: MigrationContext[C, A, FIELD_DEFAULTS],
                                                                                                     selector: Selector.Aux[FIELD_DEFAULTS, K, () => H],
                                                                                                     dT: ScopedDefaulter[C, A, T]
                                                                                                   ): ScopedDefaulter[C, A, FieldType[K, H] :: T] =
    ScopedDefaulter.instance[C, A] { a =>
      field[K](selector(ctx.fieldDefaults)()) :: field[K](dT.defaultFor(a))
    }

  implicit def parameterizedLazyFieldDefaulter[C, A, FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                                           implicit
                                                                                                           scope: Scope[C, A],
                                                                                                           ctx: MigrationContext[C, A, FIELD_DEFAULTS],
                                                                                                           selector: Selector.Aux[FIELD_DEFAULTS, K, (A) => H],
                                                                                                           dT: ScopedDefaulter[C, A, T]
                                                                                                         ): ScopedDefaulter[C, A, FieldType[K, H] :: T] = {
    ScopedDefaulter.instance[C, A] { a =>
      field[K](selector(ctx.fieldDefaults)(a)) :: field[K](dT.defaultFor(a))
    }
  }


  implicit def hNilDefaulter[C, A, FIELD_DEFAULTS <: HList]: ScopedDefaulter[C, A, HNil] =
    ScopedDefaulter.instance(_ => HNil)
}
