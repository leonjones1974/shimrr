package uk.camsw.shimrr.context

import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist
import shapeless.ops.record.Selector
import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric}
import uk.camsw.shimrr._

object global {

  private[shimrr] trait GlobalMigrationContext[FieldDefaults <: HList] extends MigrationContext[FieldDefaults]

  object MigrationContext {

    def apply[FieldDefaults <: HList](defaults: FieldDefaults = HNil): GlobalMigrationContext[FieldDefaults] = new GlobalMigrationContext[FieldDefaults] {
      override val fieldDefaults: FieldDefaults = defaults
    }
  }

  implicit def fromCoproduct[A, B, ARepr <: Coproduct](implicit
                                                       genA: Generic.Aux[A, ARepr],
                                                       m: Migration[ARepr, B]): Migration[A, B] =
    Migration.instance(a =>
      m.migrate(genA.to(a))
    )


  implicit def fromCoproductRepr[H, T <: Coproduct, B](implicit
                                                       mH: Migration[H, B],
                                                       mT: Migration[T, B]
                                                      ): Migration[H :+: T, B] =
    Migration.instance {
      case Inl(h) =>
        mH.migrate(h)
      case Inr(t) =>
        mT.migrate(t)
    }

  implicit def cNilMigration[T <: CNil, B]: Migration[T, B] =
    Migration.instance(a =>
      throw new RuntimeException(s"Will not happen, but did for $a")
    )


  implicit def fromProduct[
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


  implicit def literalFieldDefaulter[FieldDefaults <: HList, K <: Symbol, H, T <: HList](
                                                                                          implicit
                                                                                          ctx: GlobalMigrationContext[FieldDefaults],
                                                                                          selector: Selector.Aux[FieldDefaults, K, H],
                                                                                          dT: Defaulter[T]
                                                                                        ): Defaulter[FieldType[K, H] :: T] =
    Defaulter.instance {
      field[K](selector(ctx.fieldDefaults)) :: field[K](dT.default)
    }

  implicit def lazyLiteralFieldDefaulter[FieldDefaults <: HList, K <: Symbol, H, T <: HList](
                                                                                              implicit
                                                                                              ctx: GlobalMigrationContext[FieldDefaults],
                                                                                              selector: Selector.Aux[FieldDefaults, K, () => H],
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
