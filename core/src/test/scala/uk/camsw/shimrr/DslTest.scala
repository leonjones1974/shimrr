package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import uk.camsw.shimrr.context.scoped._
import uk.camsw.shimrr.macros.migration
import uk.camsw.shimrr.syntax._



class DslTest extends FreeSpec {

  "The dsl" - {
    "can be used to define" - {
      "a single rule at the product level" in {

        @migration
        val rules = new Dsl[Str1Str2] {

          ('intField1, 51)

        }

        import rules.exports._

        Str1Str2("str1", "str2").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 51)
      }

//      "multiple rules at the product level" in {
//        @migration
//        val rules = new Dsl[Str1] {
//
//          ('stringField2, "str2")
//          ('intField1, 51)
//
//        }
//
//        import rules.exports._
//
//        Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 51)
//      }

      "migrations that require no field defaulters" in {

      }

      "multiple migrations at the product level" in {

      }

      "single migration at the coproduct level" in {

      }

      "multiple migrations at the coproduct level" in {

      }

      "rules using arrow style tuple for field defaults" in {

      }
    }
  }
}
