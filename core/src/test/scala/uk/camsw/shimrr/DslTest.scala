package uk.camsw.shimrr

import org.scalatest.FreeSpec
import uk.camsw.shimrr.macros.migration

class DslTest extends FreeSpec {

  "The dsl" - {
    "can be used to define" - {
      "a single rule at the product level" in {

        @migration
        object Str1Rules extends Dsl {
          type FROM = Str1

          'str2 ~> "str2"

          'int1 ~> 25
        }

        import Str1Rules.exports._

//        Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)
      }
    }
  }
}
