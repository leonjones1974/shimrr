package uk.camsw.shimrr

import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric}
import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist
import shapeless.ops.record.Selector

object instances {

  implicit def cNilMigration[FIELD_DEFAULTS <: HList, T <: CNil, B <: Versioned, BRepr](implicit
                                                                                        ctx: MC[FIELD_DEFAULTS],
                                                                                        genB: LabelledGeneric.Aux[B, BRepr]
                                                                                       ): Migration[T, B] =
    Migration.instance(a =>
      throw new RuntimeException(s"Will not happen, but did for $a")
    )

  implicit def coproductReprMigration[FIELD_DEFAULTS <: HList, H, T <: Coproduct, B <: Versioned, BRepr <: HList](implicit
                                                                                                                  ctx: MC[FIELD_DEFAULTS],
                                                                                                                  genB: LabelledGeneric.Aux[B, BRepr],
                                                                                                                  mH: Migration[H, B],
                                                                                                                  mT: Migration[T, B]
                                                                                                                 ): Migration[H :+: T, B] =
    Migration.instance {
      case Inl(h) =>
        mH.migrate(h)
      case Inr(t) =>
        mT.migrate(t)
    }

  implicit def coproductMigration[FIELD_DEFAULTS <: HList, A, B <: Versioned, ARepr <: Coproduct, BRepr <: HList](implicit
                                                                                                                  ctx: MC[FIELD_DEFAULTS],
                                                                                                                  genA: Generic.Aux[A, ARepr],
                                                                                                                  genB: LabelledGeneric.Aux[B, BRepr],
                                                                                                                  m: Migration[ARepr, B]): Migration[A, B] =
    Migration.instance(a =>
      m.migrate(genA.to(a))
    )


  implicit def productMigration[
  FIELD_DEFAULTS <: HList,
  A, ARepr <: HList,
  B <: Versioned, BRepr <: HList,
  Common <: HList,
  Added <: HList,
  Unaligned <: HList](
                       implicit
                       ctx: MC[FIELD_DEFAULTS],
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

  implicit def literalRecordDefaulter[FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                            implicit
                                                                                            ctx: MC[FIELD_DEFAULTS],
                                                                                            selector: Selector.Aux[FIELD_DEFAULTS, K, H],
                                                                                            dT: Defaulter[T]
                                                                                          ): Defaulter[FieldType[K, H] :: T] =
    Defaulter.instance {
      field[K](selector(ctx.fieldDefaults)) :: field[K](dT.empty)
    }

  implicit def lazyRecordDefaulter[FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                         implicit
                                                                                         ctx: MC[FIELD_DEFAULTS],
                                                                                         selector: Selector.Aux[FIELD_DEFAULTS, K, () => H],
                                                                                         dT: Defaulter[T]
                                                                                       ): Defaulter[FieldType[K, H] :: T] = {
    Defaulter.instance {
      field[K](selector(ctx.fieldDefaults)()) :: field[K](dT.empty)
    }
  }

  implicit val hNilDefaulter: Defaulter[HNil] = new Defaulter[HNil] {
    val empty: HNil.type = HNil
  }

}
