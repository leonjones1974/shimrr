package uk.camsw.shimrr

import cats.Monoid
import cats.evidence.As
import cats.instances.int._
import cats.instances.string._
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import shapeless.{HList, LabelledGeneric, Witness, labelled}
import shapeless.labelled.{FieldType, KeyTag, field}
import shapeless.ops.record.Keys
import uk.camsw.shimrr.Migration._


class MigrationTest extends WordSpec {

  val base = BaseVersion()

  "product.migrateTo" should {

    "map matching fields to V+1" in {
      val migrated = base.migrateTo[BaseVersion]
      migrated shouldBe base
    }

    "drop removed fields - variation 1" in {
      base.migrateTo[VersionWithoutStringField1] shouldBe base.withoutStringField1
    }

    "drop removed fields - variation 2" in {
      base.migrateTo[VersionWithoutStringField2] shouldBe base.withoutStringField2
    }

    "drop removed fields - variation 3" in {
      base.migrateTo[VersionWithNoFields] shouldBe base.withNoFields
    }
    //

    "move fields" in {
      base.migrateTo[VersionWithSwappedFields] shouldBe base.withSwappedFields
    }

    /*    "add missing fields - using monoid" in {
          VersionWithoutStringField1("str2", 32).migrateTo[BaseVersion] shouldBe BaseVersion("", "str2", 32)
        }*/
  }

  "coproduct.migrateTo" should {
    val x: Version = base

    "drop removed fields - list" in {
      val xs: List[Version] = List(base, base.withoutStringField1)
      xs.migrateTo[VersionWithNoFields] shouldBe List(VersionWithNoFields(), VersionWithNoFields())
    }

    "map fields - atom" in {
      x.migrateTo[BaseVersion] shouldBe base
    }

    "drop removed fields - atom" in {
      x.migrateTo[VersionWithoutStringField1] shouldBe base.withoutStringField1
    }

    "move fields - atom" in {
      x.migrateTo[VersionWithSwappedFields] shouldBe base.withSwappedFields
    }

    "move fields - list" in {
      val xs: List[Version] = List(base, base)
      xs.migrateTo[VersionWithSwappedFields] shouldBe List(base.withSwappedFields, base.withSwappedFields)
    }

//    "add missing fields - atom - using monoid" in {
//      VersionWithNoFields.migrateTo[BaseVersion] shouldBe BaseVersion("", "", 0)
//    }

//    "add missing field - atom - using locally defined monoid" in {
//      implicit val strMonoid = new Monoid[String] {
//        override def empty = "NOT_IMPLEMENTED"
//
//        override def combine(x: String, y: String) = Monoid[String].combine(x, y)
//      }
//
//      VersionWithNoFields.migrateTo[BaseVersion] shouldBe BaseVersion("NOT_IMPLEMENTED", "NOT_IMPLEMENTED", 0)
//    }


    "add missing field - atom - using a custom monoid for a specific field but with a runtime element" in {
      implicit def stringField1Monoid[K <: Symbol, A](
                                                       implicit
                                                       mA: Monoid[A],
                                                       witK: Witness.Aux[K]
                                                     ) = new Monoid[FieldType[K, A]] {

        override def empty = witK.value match {
          case 'stringField1 =>
            field[K]("CUSTOM".asInstanceOf[A])
          case _ => field[K](mA.empty)

        }

        override def combine(x: FieldType[K, A], y: FieldType[K, A]) = ???
      }

//      implicit def kSomething[K <: Symbol] = new DoSomething[K] {
//        override def apply(a: K) = {
//          println(s"hello with :${a}")
//          "fish"
//        }
//      }
//

//      implicit def fishFoo: DoSomething['stringField1] = new DoSomething['stringField1] {
//        override def apply(a: 'stringField1): String = "got a foo"
//      }
      //
      //  implicit def fishBar: DoSomething["bar"] = new DoSomething["bar"] {
      //    override def apply(a: "bar"): String = "got a bar"
      //  }


//      Macros.helloWorld("fish")
      println("the macro says: " + Test.bar(25))
//      def sayIt(): String = {
//        "I Said it from a different scope"
//      }

      def sayIt(name: String): String = s"I said ${name}"

//      println("the hwmacro says: " + Test.helloWorld("fish"))

      val x = 'fish
      println("the hwmacro says: " + Test.helloWorld(x.name))
//      val x1SAtr = 'fish.name
//      println("the hwmacro says: " + Test.helloWorld(x1SAtr))


      VersionWithNoFields.migrateTo[VersionWithStringField1] shouldBe VersionWithStringField1("CUSTOM")
    }




//    "add missing field - atom - using a custom, type-safe monoid for a specific field" in {
//      import shapeless.syntax.singleton._
//
//
//      implicit def stringField1Monoid[A]: Monoid[FieldType["stringField1", A]] = new Monoid[FieldType["stringField1", A]] {
//
//
//        override def empty = field["stringField1"]("CUSTOM".asInstanceOf[A])
//
//        override def combine(x: FieldType["stringField1", A], y: FieldType["stringField1", A]) = ???
//      }
//
//      implicit def stringField2Monoid[A]: Monoid[FieldType["stringField2", A]] = new Monoid[FieldType["stringField2", A]] {
//
//
//        override def empty = field["stringField2"]("CUSTOM".asInstanceOf[A])
//
//        override def combine(x: FieldType["stringField2", A], y: FieldType["stringField2", A]) = ???
//      }
//
//      implicit def intField1Monoid[A]: Monoid[FieldType["intField1", A]] = new Monoid[FieldType["intField1", A]] {
//
//
//        override def empty = field["intField1"]("CUSTOM".asInstanceOf[A])
//
//        override def combine(x: FieldType["intField1", A], y: FieldType["intField1", A]) = ???
//      }
//
//      VersionWithNoFields.migrateTo[BaseVersion] shouldBe BaseVersion("CUSTOM", "", 0)
//    }

    "add missing fields - list - using monoid" in {
      val xs: List[Version] = List(VersionWithNoFields(), VersionWithoutStringField1("str2", 12), VersionWithoutStringField2("str1", 12), base)
      xs.migrateTo[BaseVersion] shouldBe List(
        BaseVersion("", "", 0),
        BaseVersion("", "str2", 12),
        BaseVersion("str1", "", 12),
        base
      )
    }
  }
}
