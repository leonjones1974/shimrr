package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.syntax._
import uk.camsw.shimrr.instances._

//COMEBACK
class ParameterizedLazyFieldTest extends FreeSpec {



  "when adding fields using a scoped context" - {
//    "defaulters can access the previous version" in {
//      val nextCount: NoFields => Int = _ => 0
//      implicit val ctx = MigrationContext.scoped[NoFields](
//        'intField1 ->> nextCount :: HNil
//      )
//
//      NoFields().migrateTo[Int1] shouldBe Int1(0)
//    }

//    "still can't use to scope hlists" in {
//      val nextCount: Str1 => Int = _ => 0
//
//      implicit val str1 = MigrationContext.scoped[Str1Str2](
//        'stringField2 ->> "str2" ::
//        'intField1 ->> 0 :: HNil
//      )
//
////      implicit val str1Str2 = MigrationContext.scoped[Str1Str2](
////        'intField1 ->> nextCount :: HNil
////      )
//
//      Str1Str2("str1", "str2").migrateTo[Str1Str2Int1] shouldBe Int1(0)
//    }
  }
}