package uk.camsw.shimrr

import shapeless.labelled._
import shapeless.ops.hlist
import shapeless.ops.record.Selector
import shapeless.{:+:, ::, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric}

import scala.collection.GenSeq
import scala.language.experimental.macros

trait Migration[A, B] {
  def migrate(a: A): B
}

object Migration {

  def lift[A, B](f: A => B): Migration[A, B] = (a: A) => f(a)

  def migrate[A, B](a: A)(implicit m: Migration[A, B]): B = m.migrate(a)

  implicit class MigrationOps[A](a: A) {
    def migrateTo[B](implicit migration: Migration[A, B]): B = migration.migrate(a)
  }

  implicit class MigrationGenSeqOps[A](xs: GenSeq[A]) {
    def migrateTo[B](implicit migration: Migration[A, B]): GenSeq[B] = xs map migration.migrate
  }

  
  implicit def cnilMigration[T <: Coproduct, B, BRepr](implicit
                                                       genB: LabelledGeneric.Aux[B, BRepr]
                                                      ): Migration[T, B] =
    Migration.lift(_ => {
      throw new RuntimeException("Will not happen")
    })

  implicit def coproductMigration[H, T <: Coproduct, B, BRepr <: HList](implicit
                                                                        genB: LabelledGeneric.Aux[B, BRepr],
                                                                        mH: Migration[H, B],
                                                                        mT: Migration[T, B]
                                                                       ): Migration[H :+: T, B] =
    Migration.lift {
      case Inl(h) =>
        println(s"Migrating using coproduct head: $h")
        val migrated = mH.migrate(h)
        println(s"Migrated coprod $h => $migrated")
        migrated
      case Inr(t) =>
        println(s"Migrating using coproduct tail: $t")
        mT.migrate(t)
    }

  implicit def genericMigration[A, B, ARepr <: Coproduct, BRepr <: HList](implicit
                                                                          genA: Generic.Aux[A, ARepr],
                                                                          genB: LabelledGeneric.Aux[B, BRepr],
                                                                          m: Migration[ARepr, B]): Migration[A, B] =
    Migration.lift(a => {
      println(s"Migrating using generic migration: $a")
      val migrated = m.migrate(genA.to(a))
      println(s"Migrated generic $a => $migrated")
      migrated
    })

  import shapeless.syntax.singleton._

  val funs2 =
    (
      'stringField1 ->> {
        "STR1"
      }) :: (
      'stringField2 ->> {
        "STR2"
      }) :: (
      'intField1 ->> {
        -99
      }) :: HNil

  trait Defaulter[A] {
    def empty: A
  }

  implicit def labelledHListMonoid[K <: Symbol, H, T <: HList](
                                                                implicit
                                                                mT: Defaulter[T],
                                                                selector: Selector.Aux[funs2.type, K, H]
                                                              ): Defaulter[FieldType[K, H] :: T] = {


    new Defaulter[FieldType[K, H] :: T] {
      val empty = {
        println("Trying to use labelled hlist empty monoid")
        println(s"selector: ${selector(funs2)}")
        field[K](selector(funs2)) :: field[K](mT.empty)
      }
    }
  }

  implicit def hListMigration[A, ARepr <: HList, B, BRepr <: HList, Common <: HList, Added <: HList, Unaligned <: HList, Mapped <: HList](implicit
                                                                                                                                          genA: LabelledGeneric.Aux[A, ARepr],
                                                                                                                                          genB: LabelledGeneric.Aux[B, BRepr],
                                                                                                                                          inter: hlist.Intersection.Aux[ARepr, BRepr, Common],
                                                                                                                                          diff: hlist.Diff.Aux[BRepr, Common, Added],
                                                                                                                                          defaulter: Defaulter[Added],
                                                                                                                                          prepend: hlist.Prepend.Aux[Added, Common, Unaligned],
                                                                                                                                          align: hlist.Align[Unaligned, BRepr]
                                                                                                                                         ): Migration[A, B] =
    Migration.lift {
      a =>
        genB.from(align(prepend(defaulter.empty, inter(genA.to(a)))))
    }


  implicit val hNilDefaulter: Defaulter[HNil] = new Defaulter[HNil] {
    val empty: HNil.type = HNil
  }


  trait DoSomething[A] {
    def apply(a: A): String
  }

}

