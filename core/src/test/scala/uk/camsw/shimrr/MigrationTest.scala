package uk.camsw.shimrr

import cats.instances.int._
import cats.instances.string._
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.context.global._
import uk.camsw.shimrr.syntax._

//class MigrationTest extends WordSpec {
//
//  implicit val ctx = MigrationContext('stringField1 ->> "STR1" ::
//    'stringField2 ->> "STR2" ::
//    'intField1 ->> -99 ::
//    HNil)
//
//
//  val base = Str1Str2Int1()
//
//  "product.migrateTo" should {
//
//    "map matching fields to V+1" in {
//      val migrated = base.migrateTo[Str1Str2Int1]
//      migrated shouldBe base
//    }
//
//    "drop removed fields - variation 1" in {
//      base.migrateTo[Str2Int1] shouldBe base.withoutStringField1
//    }
//
//    "drop removed fields - variation 2" in {
//      base.migrateTo[Str1Int1] shouldBe base.withoutStringField2
//    }
//
//    "drop removed fields - variation 3" in {
//      base.migrateTo[NoFields] shouldBe base.withNoFields
//    }
//
//    "move fields" in {
//      base.migrateTo[Str2Int1Str1] shouldBe base.withSwappedFields
//    }
//
//    "add missing fields - using monoid" in {
//      Str2Int1("str2", 32).migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("STR1", "str2", 32)
//      Int1(32).migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("STR1", "STR2", 32)
//      NoFields().migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("STR1", "STR2", -99)
//      Int1(15).migrateTo[Str2Int1] shouldBe Str2Int1("STR2", 15)
//      Str1("str1").migrateTo[Int1] shouldBe Int1(-99)
//    }
//
//  }
//
//  "coproduct.migrateTo" should {
//    val x: Version = base
//
//    "drop removed fields - list" in {
//      val xs: List[Version] = List(base, base.withoutStringField1)
//      xs.migrateTo[NoFields] shouldBe List(NoFields(), NoFields())
//    }
//
//    "map fields - atom" in {
//      x.migrateTo[Str1Str2Int1] shouldBe base
//    }
//
//    "drop removed fields - atom" in {
//      x.migrateTo[Str2Int1] shouldBe base.withoutStringField1
//    }
//
//    "move fields - atom" in {
//      x.migrateTo[Str2Int1Str1] shouldBe base.withSwappedFields
//    }
//
//    "move fields - list" in {
//      val xs: List[Version] = List(base, base)
//      xs.migrateTo[Str2Int1Str1] shouldBe List(base.withSwappedFields, base.withSwappedFields)
//    }
//
//    "add missing fields - atom - using monoid" in {
//      val x: Version = Str1("str1")
//      x.migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "STR2", -99)
//    }
//
//    "add missing fields - list - using monoid" in {
//      val xs: List[Version] = List(Str1("str1"), Int1(10), NoFields(), base)
//      xs.migrateTo[Str1Str2Int1] shouldBe List(
//        Str1Str2Int1("str1", "STR2", -99),
//        Str1Str2Int1("STR1", "STR2", 10),
//        Str1Str2Int1("STR1", "STR2", -99),
//        base
//      )
//    }
//  }
//}
