package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.context.scoped._
import uk.camsw.shimrr.syntax._

//class ParameterizedLazyFieldTest extends FreeSpec {
//
//  "when adding fields using a scoped context" - {
//    "defaulters can access the previous version" in {
//
//      val nextCount: NoFields => Int = _ => 0
//      implicit val ctx = MigrationContext[NoFields](
//        'intField1 ->> nextCount :: HNil
//      )
//
//      NoFields().migrateTo[Int1] shouldBe Int1(0)
//    }
//
//    "defaulters can access the previous version when defaulting multiple fields" in {
//      val nextCount: NoFields => Int = _ => 5
//      val str1: NoFields => String = _ => "str1"
//      val str2: NoFields => String = _ => "str2"
//
//
//      implicit val ctx = MigrationContext[NoFields](
//        'stringField1 ->> str1 ::
//          'stringField2 ->> str2 ::
//          'intField1 ->> nextCount :: HNil
//      )
//
//      NoFields().migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 5)
//    }
//
//    "arity0 and arity1 can be mixed and matched" in {
//      val arity0 = () => "str2"
//      val arity1: Str1 => Int = _ => 5
//
//      implicit val ctx = MigrationContext[Str1](
//        'stringField2 ->> arity0 ::
//          'intField1 ->> arity1 :: HNil
//      )
//
//      Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 5)
//    }
//
//    "arity0 and literal can be mixed and matched" in {
//      val arity0 = () => "str2"
//
//      implicit val ctx = MigrationContext[Str1](
//        'stringField2 ->> arity0 ::
//          'intField1 ->> 5 :: HNil
//      )
//
//      Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 5)
//    }
//
//    "arity1 and literal can be mixed and matched" in {
//      val arity1 = (_: Str1) => "str2"
//
//      implicit val ctx = MigrationContext[Str1](
//        'stringField2 ->> arity1 ::
//          'intField1 ->> 5 :: HNil
//      )
//
//      Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 5)
//    }
//
//    "arity 0, arity1 and literal can be mixed and matched" in {
//      val arity0 = () => "str1"
//      val arity1 = (_: NoFields) => "str2"
//
//      implicit val ctx = MigrationContext[NoFields](
//        'stringField1 ->> arity0 ::
//          'stringField2 ->> arity1 ::
//          'intField1 ->> 5 :: HNil
//      )
//
//      NoFields().migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 5)
//    }
//  }
//}