package uk.camsw.shimrr

import shapeless.labelled.FieldType
import shapeless.ops.hlist
import shapeless.{:+:, ::, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric, Lazy, labelled}

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

  import cats.Monoid


  implicit def hListMigration[A, ARepr <: HList, B, BRepr <: HList, Common <: HList, Added <: HList, Unaligned <: HList](implicit
                                                                                                                         genA: LabelledGeneric.Aux[A, ARepr],
                                                                                                                         genB: LabelledGeneric.Aux[B, BRepr],
                                                                                                                         inter: hlist.Intersection.Aux[ARepr, BRepr, Common],
                                                                                                                         diff: hlist.Diff.Aux[BRepr, Common, Added],
                                                                                                                         monoid: Monoid[Added],
                                                                                                                         prepend: hlist.Prepend.Aux[Added, Common, Unaligned],
                                                                                                                         align: hlist.Align[Unaligned, BRepr]
                                                                                                                        ): Migration[A, B] =
    Migration.instance {
      a =>
        println(s"Migrating using hlist migration: $a")
        val it = inter(genA.to(a))
        println(s"inter is: $it")
        //        println(s"diff is: ${diff.apply(it)}")
        //        val migrated = genB.from(align(it))
        genB.from(align(prepend(monoid.empty, inter(genA.to(a)))))
      //        println(s"Migrated hlist ${genA.to(a)} => $migrated")
      //        migrated
    }


  implicit val hnilMonoid: Monoid[HNil] = new Monoid[HNil] {
    override def empty = HNil

    override def combine(x: HNil, y: HNil) = HNil
  }


  implicit def labelledHListMonoid[K <: Symbol, H, T <: HList](
                                                                implicit
                                                                mH: Lazy[Monoid[H]],
                                                                mT: Monoid[T]
                                                              ): Monoid[FieldType[K, H] :: T] = {

    new Monoid[FieldType[K, H] :: T] {
      override def empty = {
        println("Trying to use labelled hlist empty monoid")
        val empty = labelled.field[K](mH.value.empty) :: mT.empty
        println(s"Created empty: ${empty}")
        empty
      }

      override def combine(x: ::[FieldType[K, H], T], y: ::[FieldType[K, H], T]) = {
        println("Trying to combine labelled hlist monoid")
        ???
      }
    }
  }

  implicit def hListMonoid[H, T <: HList](
                                           implicit
                                           mH: Lazy[Monoid[H]],
                                           mT: Monoid[T]
                                         ): Monoid[H :: T] = {
    println("trying to create hlist monoid")
    //        println(s"zero value for h is: ${mH.empty}")
    println(s"actual value for h is: ${mH.value.empty}")
    println(s"zero value for t is: ${mT.empty}")

    new Monoid[H :: T] {
      override def empty = mH.value.empty :: mT.empty

      override def combine(x: ::[H, T], y: ::[H, T]) =
        mH.value.combine(x.head, y.head) :: mT.combine(x.tail, y.tail)
    }
  }

}
