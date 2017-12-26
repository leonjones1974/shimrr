package uk.camsw.shimrr

import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric}
import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist
import shapeless.ops.record.Selector

object instances {

  implicit def cNilMigration[T <: CNil, B <: ReadRepair, BRepr](implicit
                                                                genB: LabelledGeneric.Aux[B, BRepr]
                                                               ): Migration[T, B] =
    Migration.instance(a =>
      throw new RuntimeException(s"Will not happen, but did for $a")
    )

  implicit def coproductReprMigration[H, T <: Coproduct, B <: ReadRepair, BRepr <: HList](implicit
                                                                                          mH: Migration[H, B],
                                                                                          mT: Migration[T, B]
                                                                                         ): Migration[H :+: T, B] =
    Migration.instance {
      case Inl(h) =>
        mH.migrate(h)
      case Inr(t) =>
        mT.migrate(t)
    }

  implicit def coproductMigration[A, B <: ReadRepair, ARepr <: Coproduct, BRepr <: HList](implicit
                                                                                          genA: Generic.Aux[A, ARepr],
                                                                                          genB: LabelledGeneric.Aux[B, BRepr],
                                                                                          m: Migration[ARepr, B]): Migration[A, B] =
    Migration.instance(a =>
      m.migrate(genA.to(a))
    )


  implicit def productMigration[
  A, ARepr <: HList,
  B <: ReadRepair, BRepr <: HList,
  Common <: HList,
  Added <: HList,
  Unaligned <: HList](
                       implicit
                       genA: LabelledGeneric.Aux[A, ARepr],
                       genB: LabelledGeneric.Aux[B, BRepr],
                       inter: hlist.Intersection.Aux[ARepr, BRepr, Common],
                       diff: hlist.Diff.Aux[BRepr, Common, Added],
                       defaulter: Defaulter[Added],
                       prepend: hlist.Prepend.Aux[Added, Common, Unaligned],
                       align: hlist.Align[Unaligned, BRepr]
                     ): Migration[A, B] =
    Migration.instance {
      a =>
        genB.from(align(prepend(defaulter.empty, inter(genA.to(a)))))
    }

  //todo: If this ever works, dry it up
  implicit def scopedProductMigration[
  A, ARepr <: HList,
  B <: ReadRepair, BRepr <: HList,
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
        genB.from(align(prepend(defaulter.empty, inter(genA.to(a)))))
    }

  implicit def literalRecordDefaulter[FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                            implicit
                                                                                            ctx: MigrationContext[FIELD_DEFAULTS],
                                                                                            selector: Selector.Aux[FIELD_DEFAULTS, K, H],
                                                                                            dT: Defaulter[T]
                                                                                          ): Defaulter[FieldType[K, H] :: T] =
    Defaulter.instance {
      field[K](selector(ctx.fieldDefaults)) :: field[K](dT.empty)
    }

  implicit def lazyRecordDefaulter[FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                         implicit
                                                                                         ctx: MigrationContext[FIELD_DEFAULTS],
                                                                                         selector: Selector.Aux[FIELD_DEFAULTS, K, () => H],
                                                                                         dT: Defaulter[T]
                                                                                       ): Defaulter[FieldType[K, H] :: T] = {
    Defaulter.instance {
      field[K](selector(ctx.fieldDefaults)()) :: field[K](dT.empty)
    }
  }

  implicit def scopedLiteralRecordDefaulter[A <: ReadRepair, FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](implicit
                                                                                                                  ctx: ScopedMigrationContext[A, FIELD_DEFAULTS],
                                                                                                                  selector: Selector.Aux[FIELD_DEFAULTS, K, H],
                                                                                                                  dT: Defaulter[T]
                                                                                                                 ): ScopedDefaulter[A, FieldType[K, H] :: T] =
    ScopedDefaulter.instance[A] {
      field[K](selector(ctx.fieldDefaults)) :: field[K](dT.empty)
    }

  implicit val hNilDefaulter: Defaulter[HNil] = new Defaulter[HNil] {
    val empty: HNil.type = HNil
  }

}
