package uk.camsw.shimrr

import cats.instances.int._
import cats.instances.string._
import org.scalatest.Matchers._
import org.scalatest.WordSpec
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

    "move fields" in {
      base.migrateTo[VersionWithSwappedFields] shouldBe base.withSwappedFields
    }

    "add missing fields - using monoid" in {
      VersionWithoutStringField1("str2", 32).migrateTo[BaseVersion] shouldBe BaseVersion("STR1", "str2", 32)
      VersionWithOnlyIntField(32).migrateTo[BaseVersion] shouldBe BaseVersion("STR1", "STR2", 32)
      VersionWithNoFields.migrateTo[BaseVersion] shouldBe BaseVersion("STR1", "STR2", -99)
      VersionWithOnlyIntField(15).migrateTo[VersionWithoutStringField1] shouldBe VersionWithoutStringField1("STR2", 15)
      VersionWithStringField1("str1").migrateTo[VersionWithOnlyIntField] shouldBe VersionWithOnlyIntField(-99)
    }
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
//      import shapeless.syntax.singleton._
//
//
//
//      VersionWithNoFields.migrateTo[VersionWithStringField1] shouldBe VersionWithStringField1("CUSTOM")
//    }

    "work you muther" in {
      import shapeless._
      import labelled._
      import ops.hlist._
      import ops.record.Selector
      import syntax.singleton._

      case class Foo(bar: String, fish: String, age: Int)
      val gen = LabelledGeneric[Foo]
      val funs =
        ('bar ->> {
          "I'm a new fish"
        }) ::
          ('fish ->> {
            "I'm an old fish"
          }) ::
          ('age ->> {
            144
          }) ::
          HNil

      object poly extends Poly1 {
        implicit def apply[K, V](
                                  implicit
                                  selector: Selector.Aux[funs.type, K, V]) =
          at[FieldType[K, V]] { v => selector(funs) }
      }

      def run(foo: Foo)(implicit mapper: Mapper[poly.type, gen.Repr]) =
        mapper(gen to foo)

      println(run(Foo("bar", "abcdefg", 12)))
      // could not find implicit value for parameter mapper: shapeless.ops.hlist.Mapper[POC.<refinement>.type,POC.gen.Repr]

    }


    "playing with literal types" in {
//      trait DoSomething[+A] {
//        def apply(a: Any): String
//      }
//      //
//      //      implicit def fishFoo: DoSomething[Symbol with Tagged["foo"]] = new DoSomething[Symbol with Tagged["foo"]] {
//      //        override def apply(a: Symbol with Tagged["foo"]): String = "got a foo"
//      //      }
//      //
//      //      implicit def fishBar: DoSomething[Symbol with Tagged["bar"]] = new DoSomething[Symbol with Tagged["bar"]] {
//      //        override def apply(a: Symbol with Tagged["bar"]): String = "got a foo"
//      //      }
//      def y[A <: Symbol](a: A)(implicit
//                               w: Witness.Aux[a.type]
//      ): String = w.value.name
//
//      def x[A <% Symbol](a: A)(
//        implicit
//        d: DoSomething[a.type]
//      ): String = d(a)
//
//
//      implicit val doSomethingBar = new DoSomething[Witness.`'bar`.T] {
//        override def apply(a: Any) = "bar bar implicit sheep"
//      }
//
//      implicit val doSomethingBars = new DoSomething[Witness.`'bars`.T] {
//        override def apply(a: Any) = "bars bars implicit blue sheep"
//      }
//
//
//      //
//      //      implicit val doSomethingBarsX = new DoSomething[Witness.`'bars`.T] {
//      //        override def apply(a: Any) = "bars bars implicit blue sheep"
//      //      }
//      //
//      implicit val doSomethingBarsXS = new DoSomething[
//      'bar
//      ]
//      {
//        override def apply(a: Any)
//
//        = "bars bars implicit blue sheep"
//      }
//
//
//      val yv: Symbol = 'bar
//      println(y(yv))
//
//      import shapeless._
//      import shapeless.syntax.singleton._
//
//      val concat = "right".narrow
//      val concat2 = 'right.narrow
//
//      //      def extract[s <: String](x: s)(implicit witness: Witness.Aux[s]): String = witness.value
//      //      def extract2[s <: Symbol](x: s)(implicit witness: Witness.Aux[s]): s = witness.value
//
//      def extract(witness: Witness.Lt[String]): String = witness.value
//
//      def extract2(witness: Witness.Lt[Symbol]): Symbol = witness.value
//
//      def extract3[A, B](witness: Witness.Lt[A])(
//        implicit sel: Selector.Aux[witness.T, String]) = {
//        "boo"
//        //        ev(witness.value)
//      }
//
//      //      println(extract("hello"))
//      //      val xx = 'fish
//      //      println(extract2(xx))
//
//      //      println(extract3(('bar -> "Fish")))
//      //      val xxx: 'bar = 'bar
//      //      println(extract3(xxx))
//      //      val yyy: xxx.type = 'bar
//      //      println(extract3(yyy))
//      //      val zzz = 'bar.narrow
//      //      println(extract3(zzz))
//      //      println(extract3('bony))
//
//      //      println(extract[concat.type](concat))
//      //      println(extract2[concat2.type](concat2))
//
//      //      extract(concat2)
//      //
//      //      val xv = 'bar
//      //      val xvf = xv.asInstanceOf[Symbol with Singleton]
//      //      println(x['bar](xvf))
//
//      //      println(x("bar"))
//      //      val xz: 'foo = 'foo
//      //      println(x(xz))
//      //      val zy = 'foo
//      //      println(x("wont compile"))
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

//    "add missing fields - list - using monoid" in {
//      val xs: List[Version] = List(VersionWithNoFields(), VersionWithoutStringField1("str2", 12), VersionWithoutStringField2("str1", 12), base)
//      xs.migrateTo[BaseVersion] shouldBe List(
//        BaseVersion("", "", 0),
//        BaseVersion("", "str2", 12),
//        BaseVersion("str1", "", 12),
//        base
//      )
//    }
  }
}
