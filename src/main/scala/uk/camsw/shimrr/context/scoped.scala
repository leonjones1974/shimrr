package uk.camsw.shimrr.context

import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist
import shapeless.ops.record.Selector
import shapeless.{::, CNil, HList, HNil, LabelledGeneric}
import uk.camsw.shimrr.{Migration, ScopedDefaulter, ScopedMigrationContext}

object scoped {

  implicit def cNilMigration[T <: CNil, B, BRepr](implicit
                                                  genB: LabelledGeneric.Aux[B, BRepr]
                                                 ): Migration[T, B] =
    Migration.instance(a =>
      throw new RuntimeException(s"Will not happen, but did for $a"))


  implicit def scopedProductMigration[
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

    implicit def scopedLiteralRecordDefaulter[A, FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                                     implicit
                                                                                                     ctx: ScopedMigrationContext[A, FIELD_DEFAULTS],
                                                                                                     selector: Selector.Aux[FIELD_DEFAULTS, K, H],
                                                                                                     dT: ScopedDefaulter[A, T]
                                                                                                   ): ScopedDefaulter[A, FieldType[K, H] :: T] =
    ScopedDefaulter.instance[A] { a =>
      field[K](selector(ctx.fieldDefaults)) :: field[K](dT.defaultFor(a))
    }

  implicit def scopedLazyRecordDefaulter[A, FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                                  implicit
                                                                                                  ctx: ScopedMigrationContext[A, FIELD_DEFAULTS],
                                                                                                  selector: Selector.Aux[FIELD_DEFAULTS, K, () => H],
                                                                                                  dT: ScopedDefaulter[A, T]
                                                                                                ): ScopedDefaulter[A, FieldType[K, H] :: T] =
    ScopedDefaulter.instance[A] { a =>
      field[K](selector(ctx.fieldDefaults)()) :: field[K](dT.defaultFor(a))
    }

  implicit def parameterizedLazyRecordDefaulter[A, FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                                         implicit
                                                                                                         ctx: ScopedMigrationContext[A, FIELD_DEFAULTS],
                                                                                                         selector: Selector.Aux[FIELD_DEFAULTS, K, (A) => H],
                                                                                                         dT: ScopedDefaulter[A, T]
                                                                                                       ): ScopedDefaulter[A, FieldType[K, H] :: T] = {
    ScopedDefaulter.instance[A] { a =>
      field[K](selector(ctx.fieldDefaults)(a)) :: field[K](dT.defaultFor(a))
    }
  }


  //todo: Do i need this?
  implicit def scopedHNilDefaulter[A, FIELD_DEFAULTS <: HList](
                                                                implicit
                                                                ctx: ScopedMigrationContext[A, FIELD_DEFAULTS],
                                                              ): ScopedDefaulter[A, HNil] =
    ScopedDefaulter.instance(_ => HNil)
}
