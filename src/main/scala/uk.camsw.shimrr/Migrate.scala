package uk.camsw.shimrr

import shapeless.ops.hlist
import shapeless.{Generic, HList, LabelledGeneric}

trait Migration[A, B] {
  def apply(a: A): B
}

object Migration {

  def apply[A, B](f: A => B) = new Migration[A, B] {
    override def apply(a: A) = f(a)
  }

  implicit class MigrationOps[A](a: A) {
    def migrateTo[B](implicit migration: Migration[A, B]): B =
      migration.apply(a)
  }

  implicit def genericMigration[A, B, ARepr, BRepr <: HList](implicit
                                                             genA: LabelledGeneric.Aux[A, ARepr],
                                                             genB: LabelledGeneric.Aux[B, BRepr],
                                                             m: Migration[ARepr, B]): Migration[A, B] =
    Migration(a => {
      println(s"Migrating using generic migration: ${a}")

      m(genA.to(a))
    })

  implicit def hListMigration[A <: HList, B, BRepr <: HList](
                                                              implicit
                                                              genB: LabelledGeneric.Aux[B, BRepr],
                                                              inter: hlist.Intersection.Aux[A, BRepr, BRepr]
                                                            ): Migration[A, B] =
    Migration {
      case repr =>
        println(s"Migrating using hlist migration: ${repr}")
        val it = (inter(repr))
        println(s"inter is: ${it}")
        genB.from(it)
    }


}
