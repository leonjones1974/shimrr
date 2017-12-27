package uk.camsw.shimrr.context

import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist
import shapeless.ops.record.Selector
import shapeless.{::, CNil, HList, HNil, LabelledGeneric}
import uk.camsw.shimrr.Migration

object scoped {

  private[shimrr] trait ScopedMigrationContext[A , FIELD_DEFAULTS <: HList] extends MigrationContext[FIELD_DEFAULTS]

  object MigrationContext {

    class ScopedBuilder[A ] {
      def apply[FIELD_DEFAULTS <: HList](defaults: FIELD_DEFAULTS): ScopedMigrationContext[A, FIELD_DEFAULTS] = new ScopedMigrationContext[A, FIELD_DEFAULTS] {
        override val fieldDefaults: FIELD_DEFAULTS = defaults
      }
    }

    def apply[A] = new ScopedBuilder[A]
  }


  implicit def cNilMigration[T <: CNil, B]: Migration[T, B] =
    Migration.instance(a =>
      throw new RuntimeException(s"Will not happen, but did for $a"))


  implicit def productMigration[
  A, ARepr <: HList,
  B, BRepr <: HList,
  Common <: HList,
  Added <: HList,
  Unaligned <: HList](
                       implicit
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

  implicit def literalFieldDefaulter[A, FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                              implicit
                                                                                              ctx: ScopedMigrationContext[A, FIELD_DEFAULTS],
                                                                                              selector: Selector.Aux[FIELD_DEFAULTS, K, H],
                                                                                              dT: ScopedDefaulter[A, T]
                                                                                            ): ScopedDefaulter[A, FieldType[K, H] :: T] =
    ScopedDefaulter.instance[A] { a =>
      field[K](selector(ctx.fieldDefaults)) :: field[K](dT.defaultFor(a))
    }

  implicit def lazyLiteralFieldDefaulter[A, FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                                  implicit
                                                                                                  ctx: ScopedMigrationContext[A, FIELD_DEFAULTS],
                                                                                                  selector: Selector.Aux[FIELD_DEFAULTS, K, () => H],
                                                                                                  dT: ScopedDefaulter[A, T]
                                                                                                ): ScopedDefaulter[A, FieldType[K, H] :: T] =
    ScopedDefaulter.instance[A] { a =>
      field[K](selector(ctx.fieldDefaults)()) :: field[K](dT.defaultFor(a))
    }

  implicit def parameterizedLazyFieldDefaulter[A, FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                                         implicit
                                                                                                         ctx: ScopedMigrationContext[A, FIELD_DEFAULTS],
                                                                                                         selector: Selector.Aux[FIELD_DEFAULTS, K, (A) => H],
                                                                                                         dT: ScopedDefaulter[A, T]
                                                                                                       ): ScopedDefaulter[A, FieldType[K, H] :: T] = {
    ScopedDefaulter.instance[A] { a =>
      field[K](selector(ctx.fieldDefaults)(a)) :: field[K](dT.defaultFor(a))
    }
  }


  implicit def hNilDefaulter[A, FIELD_DEFAULTS <: HList]: ScopedDefaulter[A, HNil] =
    ScopedDefaulter.instance(_ => HNil)
}
