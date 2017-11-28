package uk.camsw.shimrr

import shapeless.ops.hlist
import shapeless.{:+:, CNil, Coproduct, Generic, HList, Inl, Inr, LabelledGeneric}

import scala.collection.GenSeq

trait Migration[A, B] {
  def migrate(a: A): B
}

object Migration {

  def instance[A, B](f: A => B) = new Migration[A, B] {
    override def migrate(a: A) = f(a)
  }

  def migrate[A, B](a: A)(implicit
                          m: Migration[A, B]
  ): B = m.migrate(a)

  implicit class MigrationOps[A](a: A) {
    def migrateTo[B](implicit migration: Migration[A, B]): B =
      migration.migrate(a)
  }

  implicit class MigrationGenSeqOps[A](xs: GenSeq[A]) {
    def migrateTo[B](implicit migration: Migration[A, B]): GenSeq[B] =
      xs map migration.migrate
  }

  implicit def genericMigration[A, B, ARepr, BRepr <: HList](implicit
                                                             genA: Generic.Aux[A, ARepr],
                                                             genB: LabelledGeneric.Aux[B, BRepr],
                                                             m: Migration[ARepr, B]): Migration[A, B] =
    Migration.instance(a => {
      println(s"Migrating using generic migration: ${a}")

      m.migrate(genA.to(a))
    })


  implicit def hListMigration[A, ARepr <: HList, B, BRepr <: HList](
                                                                     implicit
                                                                     genA: LabelledGeneric.Aux[A, ARepr],
                                                                     genB: LabelledGeneric.Aux[B, BRepr],
                                                                     inter: hlist.Intersection.Aux[ARepr, BRepr, BRepr]
                                                                   ): Migration[A, B] =
    Migration.instance {
      case a =>
        println(s"Migrating using hlist migration: ${a}")
        val it = (inter(genA.to(a)))
        println(s"inter is: ${it}")
        genB.from(it)
    }

  implicit def cNilMigration[B, BRepr <: HList](
                                                 implicit genB: LabelledGeneric.Aux[B, BRepr]
                                               ): Migration[CNil, B] = Migration.instance(
    _ => throw new RuntimeException("Never going to occur")
  )

  implicit def coproductMigration[H, T <: Coproduct, B, BRepr <: HList](
                                                                         implicit
                                                                         genB: LabelledGeneric.Aux[B, BRepr],
                                                                         hm: Migration[H, B],
                                                                         ht: Migration[T, B]
                                                                       ): Migration[H :+: T, B] = Migration.instance {
    case Inl(h) =>
      println(s"Using coprod: ${h}")
      hm.migrate(h)
    case Inr(t) =>
      println(s"Using coprod: ${t}")
      ht.migrate(t)
  }
}
