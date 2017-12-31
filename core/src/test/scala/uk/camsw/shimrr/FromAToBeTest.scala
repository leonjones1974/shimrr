package uk.camsw.shimrr

import org.scalatest.FreeSpec
import uk.camsw.shimrr.macros.migration


class FromAToBeTest extends FreeSpec {

//  "It is possible to migrate" - {
//
//    "specifying rules for a specific target" in {
//
//      @migration
//      val no_str1 = new Dsl[NoFields, Str1] {
//
//        'str1 -> "str1"
//
//      }
//
//      @migration
//      val no_str2 = new Dsl[NoFields, Str1Str2] {
//
//        'str1 -> "str1a"
//
//        'str2 -> "str2a"
//      }
//
//      import no_str1.exports._
//      import no_str2.exports._
//      import syntax._
//      import context.scoped._
//
//      NoFields().migrateTo[Str1] shouldBe Str1Str2("str1")
//      NoFields().migrateTo[Str1Str2] shouldBe Str1Str2("str1a", "str2a")
//    }
//
//    "via composed rules" - {
//
//      @migration
//      val no_str1 = new Dsl[NoFields, Str1] {
//
//        'str1 -> "str1"
//
//      }
//
//      @migration
//      val str1_str2 = new Dsl[Str1, Str2] {
//
//        'str2 -> ((s1: Str1) => s1.stringField1.toUpperCase + "_transformed")
//      }
//
//      import no_str1.exports._
//      import str1_str2.exports._
//      import syntax._
//      import context.scoped._
//
//      NoFields().migrateTo[Str2]
//
//    }
//  }
//}

}
