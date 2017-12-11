package uk.camsw.shimrr

import shapeless.labelled.FieldType
import shapeless.ops.hlist
import shapeless.{:+:, ::, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric, Witness}

import scala.collection.GenSeq
import scala.language.experimental.macros

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
      println(s"Migrated generic $a => $migrated")
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
    override def empty: HNil.type = HNil

    override def combine(x: HNil, y: HNil): HNil.type = HNil
  }


//  implicit def fieldMonoid[K <: Symbol, A](
//                                          implicit
//                                          mA: Monoid[A]
//                                          ): Monoid[FieldType[K, A]] = new Monoid[FieldType[K, A]] {
//    override def empty = {
//      println("Using empty for field monoid")
//      field[K](mA.empty)
//    }
//
//    override def combine(x: FieldType[K, A], y: FieldType[K, A]) =
//      field[K](mA.combine(x, y))
//  }

  trait DoSomething[A] {
    def apply(a: A): String
  }




  implicit def labelledHListMonoid[K <: Symbol, H, T <: HList](
                                                                implicit
//                                                                mH: Lazy[Monoid[FieldType[K, H]]],
//                                                                mT: Monoid[T],
                                                                witness: Witness.Aux[K]
                                                              ): Monoid[FieldType[K, H] :: T] = {


    new Monoid[FieldType[K, H] :: T] {
      override def empty = {
        println("Trying to use labelled hlist empty monoid")
        println(s"witness: ${witness.value}")
        val w = witness.value
        type wT = w.type

        println(s"w is: ${w}")
//        Macros.helloWorld("f")

//        val zzz: 'stringField1 = 'stringField1
        //
//        println(s"the monoid is: ${m(witness.value)}")

//        val empty = mH.value.empty :: mT.empty




//        implicit def m: Monoid[FieldType['stringField1, H]] = new Monoid[FieldType['stringField1, H]] {
//          override def empty = field['stringField1]("CUSTOM FOR 1".asInstanceOf[H])
//
//          override def combine(x: FieldType['stringField1, H], y: FieldType['stringField1, H]) = ???
//        }
//
//        implicit def m2: Monoid[FieldType['stringField2, H]] = new Monoid[FieldType['stringField2, H]] {
//          override def empty = field['stringField2]("CUSTOM FOR 2".asInstanceOf[H])
//
//          override def combine(x: FieldType['stringField2, H], y: FieldType['stringField2, H]) = ???
//        }
//
//
//
//        implicit def m3: Monoid[FieldType['intField1, H]] = new Monoid[FieldType['intField1, H]] {
//          override def empty = field['intField1](25.asInstanceOf[H])
//
//          override def combine(x: FieldType['intField1, H], y: FieldType['intField1, H]) = ???
//        }
//
//        implicit def m4: Monoid[FieldType[Symbol, H]] = new Monoid[FieldType[Symbol, H]] {
//          override def empty = field[Symbol](25.asInstanceOf[H])
//
//          override def combine(x: FieldType[Symbol, H], y: FieldType[Symbol, H]) = ???
//        }
//
//        implicit def m5: Monoid[FieldType[k.type, H]] = new Monoid[FieldType[k.type, H]] {
//          override def empty = field[k.type](25.asInstanceOf[H])
//
//          override def combine(x: FieldType[k.type, H], y: FieldType[k.type, H]) = ???
//        }

//        implicit val mmm = new Monoid[Witness.`"100"`.T] {
//          override def empty = "100"
//          override def combine(x: "100", y: "100") = ???
//        }
//
//        def printIt[A <: Symbol](a: A)(implicit ev: Monoid[Witness.`"100"`.T]): Unit = {
//          println(s"got in here for: ${a} with ${ev.empty}")
//        }
//
//        printIt('hello)
//        printIt(witness.value)


//        val mon = implicitly[Monoid[FieldType['intField1, H]]]
//        val narrowed = witness.value.narrow
//        println(s"witness is: ${witness.value} of type ${witness.value.getClass}")
//        println(s"narrowed is: ${narrowed} of type ${narrowed.getClass}")
//
//        val monX = implicitly[Monoid[FieldType['intField1 , H]]]
//        val k:Symbol  = witness.value.asInstanceOf[Symbol]
//        val kType: k.type = k
//        println(s"k is: ${k}")
//        println(s"k type is: ${kType}")
//        type KTtype = 'stringField2
//        type KKType = k.type
//
//        val monY = implicitly[Monoid[FieldType[KTtype , H]]]
//
//        println(s"MonY says: ${monY.empty}")
//
//        println(s"the mon says: ${mon.empty}")
//        println(s"Created empty: $empty")
        println(s"The value of k is: ${witness.value}")
//        empty
        ???
      }

      override def combine(x: ::[FieldType[K, H], T], y: ::[FieldType[K, H], T]) = {
        println("Trying to combine labelled hlist monoid")
//        mH.value.combine(x.head, y.head) :: mT.combine(x.tail, y.tail)
        ???
      }
    }
  }

}

class LimitedString[Limit <: Int] (val s: String) extends AnyVal {
  override def toString = s
}
class LimitedStringCompanion[Limit <: Int : Witness.Aux]{
  def limit: Int = implicitly[Witness.Aux[Limit]].value

  def unapply(s: String): Option[LimitedString[Limit]] = {
    if(s.length > limit) None else Some(new LimitedString(s))
  }

  def truncate(s: String): LimitedString[Limit] = new LimitedString(s take limit)
}

object MyLibraryThing {
  type Name = LimitedString[Witness.`50`.T]
  object Name extends LimitedStringCompanion[Witness.`50`.T]

  def rename(id: Int, name: Name) = ???
}

