package uk.camsw.shimrr

import cats.instances.int._
import cats.instances.string._
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.context.global._
import uk.camsw.shimrr.syntax._

class MigrationTest extends WordSpec {

  implicit val ctx = MigrationContext('stringField1 ->> "STR1" ::
    'stringField2 ->> "STR2" ::
    'intField1 ->> -99 ::
    HNil)


  val base = Str1Str2Int1()

  "product.migrateTo" should {

    "map matching fields to V+1" in {
      val migrated = base.migrateTo[Str1Str2Int1]
      migrated shouldBe base
    }

    "drop removed fields" in {
      base.migrateTo[NoFields] shouldBe base.withNoFields
    }


    "move fields" in {
      trait V
      case class V1(str1: String, str2: String)
      case class V2(str2: String, str1: String)

      V1("str1", "str2").migrateTo[V2] shouldBe V2("str2", "str1")
    }
  }

  "coproduct.migrateTo" should {
    val x: Version = base

    "drop removed fields - list" in {
      val xs: List[Version] = List(base, Str1("str1"))
      xs.migrateTo[NoFields] shouldBe List(NoFields(), NoFields())
    }

    "map fields - atom" in {
      x.migrateTo[Str1Str2Int1] shouldBe base
    }

    "drop removed fields - atom" in {
      x.migrateTo[NoFields] shouldBe NoFields()
    }

    "add missing fields - atom - using monoid" in {
      val x: Version = Str1("str1")
      x.migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "STR2", -99)
    }

    "add missing fields - list - using monoid" in {
      val xs: List[Version] = List(Str1("str1"), NoFields(), base)
      xs.migrateTo[Str1Str2Int1] shouldBe List(
        Str1Str2Int1("str1", "STR2", -99),
        Str1Str2Int1("STR1", "STR2", -99),
        base
      )
    }
  }
}
