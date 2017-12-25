package uk.camsw.shimrr

import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric}
import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist
import shapeless.ops.record.Selector

trait MigrationContext[FIELD_DEFAULTS <: HList] {

  val fieldDefaults: FIELD_DEFAULTS


  implicit def cNilMigration[T <: CNil, B <: Versioned, BRepr](implicit
                                                               genB: LabelledGeneric.Aux[B, BRepr]
                                                              ): Migration[T, B] =
    Migration.instance(a =>
      throw new RuntimeException(s"Will not happen, but did for $a")
    )

  implicit def coproductReprMigration[H, T <: Coproduct, B <: Versioned, BRepr <: HList](implicit
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

  implicit def coproductMigration[A, B <: Versioned, ARepr <: Coproduct, BRepr <: HList](implicit
                                                                                         genA: Generic.Aux[A, ARepr],
                                                                                         genB: LabelledGeneric.Aux[B, BRepr],
                                                                                         m: Migration[ARepr, B]): Migration[A, B] =
    Migration.instance(a =>
      m.migrate(genA.to(a))
    )


  implicit def productMigration[
  A, ARepr <: HList,
  B <: Versioned, BRepr <: HList,
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

  implicit def literalRecordDefaulter[K <: Symbol, H, T <: HList](
                                                                   implicit
                                                                   selector: Selector.Aux[FIELD_DEFAULTS, K, H],
                                                                   dT: Defaulter[T]
                                                                 ): Defaulter[FieldType[K, H] :: T] =
    Defaulter.instance {
      field[K](selector(fieldDefaults)) :: field[K](dT.empty)
    }

  implicit def lazyRecordDefaulter[K <: Symbol, H, T <: HList](
                                                             implicit
                                                             selector: Selector.Aux[FIELD_DEFAULTS, K, () => H],
                                                             dT: Defaulter[T]
                                                           ): Defaulter[FieldType[K, H] :: T] = {
    Defaulter.instance {
      field[K](selector(fieldDefaults)()) :: field[K](dT.empty)
    }
  }

  implicit val hNilDefaulter: Defaulter[HNil] = new Defaulter[HNil] {
    val empty: HNil.type = HNil
  }

}

