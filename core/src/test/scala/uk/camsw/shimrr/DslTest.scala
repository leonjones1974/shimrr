package uk.camsw.shimrr

import org.scalatest.FreeSpec
import shapeless.{HNil, LabelledGeneric}
import uk.camsw.shimrr.macros.{Macro, migration, migrationF}
import org.scalatest.Matchers._

import scala.language.higherKinds



class DslTest extends FreeSpec {

  "The dsl" - {
    "can be used to define" - {
      "a single rule at the product level" in {

        //        @migration
        //        object Str1Rules extends Dsl[Str1] {
        //
        //          override val fieldDefaults =
        //            'stringField2 ->> "str2" ::
        //              'intField1 ->> 25 ::
        //              HNil
        //        }



//        @migration2
//        class Rules[Str1Str2] {
//          ('intField1, 51)
//        }
//        Macro.dsl[Str1Str2]{
//          //todo: Make this work with ->
//          ('intField1, 12)
//        }

//        println(s"z: ${z}")
//        println(TheRules())

        @migrationF
        val rules = new Dsl[Str1Str2] {


          // generate a case class from the tuple using old-school macros
          // call a func in the dsl to turn it into a labelled gen
          // use it for the implicit
          // expose the imports and we're done!
          // consider whether the dsl adds anything at definition time - should we just put the FROM type as before in here?
          // what about the lazy types etc




          ('intField1, 51)
//          'stringField1 -> uk.camsw.shimrr.Funcs.myLazy
//          'stringField2 -> ((p: Str1Str2) => p.stringField1 + p.stringField2)

          // Generates
//          case class Str1Str2FieldDefaults(
//                                            intField1: Int = 51,
//                                            stringField1: () => String = () => "str1",
//                                            stringField2: Str1Str2 => String = (p: Str1Str2) => p.stringField1 + p.stringField2)

//          "intField1" -> 51
//          "intField1" -> 51
//          "intField1" -> 51
//          "intField1" -> 51

          //          case class FieldDefaults(intField1: Int = 51)

//          override val fieldDefaults = HNil //f(FieldDefaults())
        }


        //todo: Be nice to import these automatically!
        //        import Str1Rules.exports._
        import syntax._
        import rules.exports._

//        println(s"class is: ${MyClass()}")
//
//        val rule = MyClass()
//        val gen = LabelledGeneric[MyClass]
//        val r = gen to rule
//        println(s"r: ${r}")
//        import context.scoped._
//        implicit val ctx = MigrationContext[Str1Str2](r)

//        println(s"fish: ${x}")

//
//        import Str1Str2Rules._
//        import StrStr2Rules.exports._
//        println(s"fish is: ${rules.exports.fish}")
        println(s"My class: ${x}")
//        println(s"zz is :$zz")
        import context.scoped._

        Str1Str2("str1", "str2").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 51)


//        println(s"the param back out: ${MyNewClass()}")



        //        Str1("str1").migrateTo[Str1] shouldBe Str1("str1")
        //        Str1("str1").migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
        //        Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)
        //
        //        Str1Str2("str1", "str2").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 51)

        //        @migration
        //        object CatchAllRules extends Dsl[Version] {
        //
        //          override val fieldDefaults =
        //            'stringField1 ->> "def String 1" ::
        //              'stringField2 ->> "def String 2" ::
        //              'intField1 ->> 99 ::
        //              HNil
        //        }

        //todo: overlapping/ hierarchical scopes?
        //        import CatchAllRules.exports._
        //        NoFields().migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("def String 1", "def String 2", 99)
      }
    }
  }
}
