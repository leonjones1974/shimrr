package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.macros.migration

class DslTest extends FreeSpec {

  "The dsl" - {
    "can be used to define" - {
      "a single rule at the product level" in {

        @migration
        object Str1Rules extends Dsl[Str1] {

          override val fieldDefaults =
            'stringField2 ->> "str2" ::
              'intField1 ->> 25 ::
              HNil
        }

        @migration
        object Str1Str2Rules extends Dsl[Str1Str2] {

          // todo: must be a nicer way than this
          override val fieldDefaults =
            'intField1 ->> 51 ::
              HNil
        }


        //todo: Be nice to import these automatically!
        import Str1Rules.exports._
        import syntax._
        import uk.camsw.shimrr.context.scoped._
        import Str1Str2Rules.exports._


        Str1("str1").migrateTo[Str1] shouldBe Str1("str1")
        Str1("str1").migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
        Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)

        Str1Str2("str1", "str2").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 51)

        @migration
        object CatchAllRules extends Dsl[Version] {

          override val fieldDefaults =
            'stringField1 ->> "def String 1" ::
              'stringField2 ->> "def String 2" ::
              'intField1 ->> 99 ::
              HNil
        }

        //todo: overlapping/ hierarchical scopes?
        //        import CatchAllRules.exports._
        //        NoFields().migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("def String 1", "def String 2", 99)
      }
    }
  }
}
