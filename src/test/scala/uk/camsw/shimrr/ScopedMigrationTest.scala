package uk.camsw.shimrr

import org.scalatest.FreeSpec
import shapeless.{HList, HNil}
import shapeless.syntax.singleton.mkSingletonOps
import syntax._
import org.scalatest.Matchers._

object ScopedMigrationRules {

  private[shimrr] val noFieldsRules = 'stringField1 ->> "str1" :: HNil
  private[shimrr] val str1Rules = 'stringField2 ->> "str2" :: HNil
}

import ScopedMigrationRules._

class ScopedMigrationTest extends FreeSpec {

  "Migration rules can be scoped" - {


//    "like this" in {
//      new MigrationContext[noFieldsRules.type] {
//        val fieldDefaults: DEFAULTS = noFieldsRules
//        NoFields().migrateTo[Str1] shouldBe Str1("str1")
//      }
//    }

    "perhaps like this" in {
      import instances._
      {
        implicit val ctx = MC(noFieldsRules)
        NoFields().migrateTo[ctx.OUT, Str1] shouldBe Str1("str1")
      }
      {
        implicit val ctx = MC(str1Rules)
        Str1("str1").migrateTo[ctx.OUT, Str1Str2] shouldBe Str1Str2("str1", "str2")
      }



    }

//    "or like this" in {
//      new MigrationContext[str1Rules.type] {
//        val fieldDefaults: DEFAULTS = str1Rules
//        Str1("str1").migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
//      }
//    }

//    "or even better, like this" in {
//      migrationContext(noFieldsRules) {
//        DslOps.evalWithinContext(NoFields().migrateTo[Str1])
//
//      }
//    }

  }
}
