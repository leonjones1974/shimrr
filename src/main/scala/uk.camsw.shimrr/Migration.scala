package uk.camsw.shimrr

import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist
import shapeless.ops.record.Selector
import shapeless.{:+:, ::, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric}

import scala.language.experimental.macros

trait Defaulter[A] {
  def empty: A
}

trait Migration[A, B] {
  def migrate(a: A): B
}

object Migration {

  def instance[A, B](f: A => B): Migration[A, B] = (a: A) => f(a)

  def migrate[A, B](a: A)(implicit m: Migration[A, B]): B = m.migrate(a)

}

trait MigrationContext {

  protected type FIELD_DEFAULTS <: HList

  protected def fieldDefaults: FIELD_DEFAULTS

  implicit def cNilMigration[T <: Coproduct, B, BRepr](implicit
                                                       genB: LabelledGeneric.Aux[B, BRepr]
                                                      ): Migration[T, B] =
    Migration.instance(a =>
      throw new RuntimeException(s"Will not happen, but did for $a")
    )

  implicit def coproductReprMigration[H, T <: Coproduct, B, BRepr <: HList](implicit
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

  implicit def coproductMigration[A, B, ARepr <: Coproduct, BRepr <: HList](implicit
                                                                            genA: Generic.Aux[A, ARepr],
                                                                            genB: LabelledGeneric.Aux[B, BRepr],
                                                                            m: Migration[ARepr, B]): Migration[A, B] =
    Migration.instance(a =>
      m.migrate(genA.to(a))
    )


  implicit def productMigration[A, ARepr <: HList, B, BRepr <: HList, Common <: HList, Added <: HList, Unaligned <: HList, Mapped <: HList](implicit
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

  implicit def recordDefaulter[K <: Symbol, H, T <: HList](
                                                            implicit
                                                            mT: Defaulter[T],
                                                            selector: Selector.Aux[FIELD_DEFAULTS, K, H]
                                                          ): Defaulter[FieldType[K, H] :: T] = {


    new Defaulter[FieldType[K, H] :: T] {
      val empty: ::[FieldType[K, H], FieldType[K, T]] = {
        field[K](selector(fieldDefaults)) :: field[K](mT.empty)
      }
    }
  }


  implicit val hNilDefaulter: Defaulter[HNil] = new Defaulter[HNil] {
    val empty: HNil.type = HNil
  }

}

