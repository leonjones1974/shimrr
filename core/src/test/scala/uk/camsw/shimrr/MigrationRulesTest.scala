package uk.camsw.shimrr

import org.scalatest.FreeSpec
import shapeless.HNil
import uk.camsw.shimrr.macros.{MacroUtil, main, sayHello}



class MigrationRulesTest extends FreeSpec {



  "the dsl" - {
    "can summon" - {
      "a scoped context" - {
        "with field defaulters" in {

          import syntax._

////          @migration
          @sayHello
          trait TestMacroExpansion[Str1] {
//              println("Hello Scala.meta macros!")
          }

//          val amp = 10
//
//          trait Something[A]
//          MacroUtil.checkType[String]()
//          val imp = 10

//
//          val fish = new Fish[Str1]()
//          val z = fish.doSomething[String]()
//          println(s"the fish did: $z")


          val strRules = new MigrationRules[Str1](HNil)

          println("scope: " + new MigrationRules[Str1](HNil).scope)


//          Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 12)


        }

      }
    }
  }
}
