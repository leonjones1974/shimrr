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

          override val fieldDefaults =
              'intField1 ->> 51 ::
              HNil
        }

        import Str1Rules.exports._
        import syntax._
        import uk.camsw.shimrr.context.scoped._
        import Str1Str2Rules.exports._

        Str1("str1").migrateTo[Str1] shouldBe Str1("str1")
        Str1("str1").migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
        Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)

        Str1Str2("str1", "str2").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 51)
      }
    }
  }
}