package uk.camsw.shimrr

import shapeless.ops.hlist
import shapeless.{:+:, Coproduct, Generic, HList, Inl, Inr, LabelledGeneric}

import scala.collection.GenSeq

trait Migration[A, B] {
  def migrate(a: A): B
}

object Migration {

  def instance[A, B](f: A => B): Migration[A, B] = new Migration[A, B] {
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


  implicit def cnilMigration[T <: Coproduct, B, BRepr](implicit
                                                       genB: LabelledGeneric.Aux[B, BRepr]
                                                      ): Migration[T, B] =
    Migration.instance(_ => {
      throw new RuntimeException("Will not happen")
    })

  implicit def coproductMigration[H, T <: Coproduct, B, BRepr <: HList](implicit
                                                                        genB: LabelledGeneric.Aux[B, BRepr],
                                                                        mH: Migration[H, B],
                                                                        mT: Migration[T, B]
                                                                       ): Migration[H :+: T, B] =
    Migration.instance {
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
    Migration.instance(a => {
      println(s"Migrating using generic migration: $a")
      val migrated = m.migrate(genA.to(a))
      println(s"Migrated generic ${a} => ${migrated}")
      migrated
    })


  implicit def hListMigration[A, ARepr <: HList, B, BRepr <: HList, Common <: HList](implicit
                                                                                     genA: LabelledGeneric.Aux[A, ARepr],
                                                                                     genB: LabelledGeneric.Aux[B, BRepr],
                                                                                     inter: hlist.Intersection.Aux[ARepr, BRepr, Common],
                                                                                     align: hlist.Align[Common, BRepr]
                                                                                    ): Migration[A, B] =
    Migration.instance {
      a =>
        println(s"Migrating using hlist migration: $a")
        val it = inter(genA.to(a))
        println(s"inter is: $it")
        val migrated = genB.from(align(it))
        println(s"Migrated hlist ${genA.to(a)} => $migrated")
        migrated
    }
}
