package uk.camsw.shimrr.context

import shapeless.labelled.{FieldType, field}
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric}
import shapeless.ops.hlist
import shapeless.ops.record.Selector
import uk.camsw.shimrr._

object global {

  implicit def coproductMigration[A, B, ARepr <: Coproduct](implicit
                                                            genA: Generic.Aux[A, ARepr],
                                                            m: Migration[ARepr, B]): Migration[A, B] =
    Migration.instance(a =>
      m.migrate(genA.to(a))
    )


  implicit def coproductReprMigration[H, T <: Coproduct, B, BRepr <: HList](implicit
                                                                            mH: Migration[H, B],
                                                                            mT: Migration[T, B]
                                                                           ): Migration[H :+: T, B] =
    Migration.instance {
      case Inl(h) =>
        mH.migrate(h)
      case Inr(t) =>
        mT.migrate(t)
    }

  implicit def cNilMigration[T <: CNil, B, BRepr]: Migration[T, B] =
    Migration.instance(a =>
      throw new RuntimeException(s"Will not happen, but did for $a")
    )


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
                       defaulter: Defaulter[Added],
                       prepend: hlist.Prepend.Aux[Added, Common, Unaligned],
                       align: hlist.Align[Unaligned, BRepr]
                     ): Migration[A, B] =
    Migration.instance {
      a =>
        genB.from(align(prepend(defaulter.default, inter(genA.to(a)))))
    }


  implicit def literalRecordDefaulter[FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                            implicit
                                                                                            ctx: GlobalMigrationContext[FIELD_DEFAULTS],
                                                                                            selector: Selector.Aux[FIELD_DEFAULTS, K, H],
                                                                                            dT: Defaulter[T]
                                                                                          ): Defaulter[FieldType[K, H] :: T] =
    Defaulter.instance {
      field[K](selector(ctx.fieldDefaults)) :: field[K](dT.default)
    }

  implicit def lazyRecordDefaulter[FIELD_DEFAULTS <: HList, K <: Symbol, H, T <: HList](
                                                                                         implicit
                                                                                         ctx: GlobalMigrationContext[FIELD_DEFAULTS],
                                                                                         selector: Selector.Aux[FIELD_DEFAULTS, K, () => H],
                                                                                         dT: Defaulter[T]
                                                                                       ): Defaulter[FieldType[K, H] :: T] = {
    Defaulter.instance {
      field[K](selector(ctx.fieldDefaults)()) :: field[K](dT.default)
    }
  }

  implicit val hNilDefaulter: Defaulter[HNil] = new Defaulter[HNil] {
    val default: HNil.type = HNil
  }

}
