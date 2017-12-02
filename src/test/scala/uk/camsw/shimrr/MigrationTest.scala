package uk.camsw.shimrr

import org.scalatest.Matchers._
import org.scalatest.WordSpec
import uk.camsw.shimrr.Migration._

import cats.instances.int._
import cats.instances.string._

class MigrationTest extends WordSpec {
  val base = BaseVersion()

  "product.migrateTo" should {

    "map matching fields to V+1" in {
      val migrated = base.migrateTo[BaseVersion]
      migrated shouldBe base
    }

    "drop removed fields - variation 1" in {
      base.migrateTo[VersionWithoutStringField1] shouldBe base.withoutStringField1
    }

    "drop removed fields - variation 2" in {
      base.migrateTo[VersionWithoutStringField2] shouldBe base.withoutStringField2
    }

    "drop removed fields - variation 3" in {
      base.migrateTo[VersionWithNoFields] shouldBe base.withNoFields
    }
    //

    "move fields" in {
      base.migrateTo[VersionWithSwappedFields] shouldBe base.withSwappedFields
    }

    "add missing fields - using monoid" in {
      VersionWithoutStringField1("str2", 32).migrateTo[BaseVersion] shouldBe BaseVersion("", "str2", 32)
    }
  }

  "coproduct.migrateTo" should {
    val x: Version = base

    "drop removed fields - list" in {
      val xs: List[Version] = List(base, base.withoutStringField1)
      xs.migrateTo[VersionWithNoFields] shouldBe List(VersionWithNoFields(), VersionWithNoFields())
    }

    "map fields - atom" in {
      x.migrateTo[BaseVersion] shouldBe base
    }

    "drop removed fields - atom" in {
      x.migrateTo[VersionWithoutStringField1] shouldBe base.withoutStringField1
    }

    "move fields - atom" in {
      x.migrateTo[VersionWithSwappedFields] shouldBe base.withSwappedFields
    }

    "move fields - list" in {
      val xs: List[Version] = List(base, base)
      xs.migrateTo[VersionWithSwappedFields] shouldBe List(base.withSwappedFields, base.withSwappedFields)
    }

    "add missing fields - atom - using monoid" in {
      VersionWithNoFields.migrateTo[BaseVersion] shouldBe BaseVersion("", "", 0)
    }

    "add missing fields - list - using monoid" in {
      import cats.instances.int._
      import cats.instances.string._

      val xs: List[Version] = List(VersionWithNoFields(), VersionWithoutStringField1("str2", 12), VersionWithoutStringField2("str1", 12), base)
      xs.migrateTo[BaseVersion] shouldBe List(
        BaseVersion("", "", 0),
        BaseVersion("", "str2", 12),
        BaseVersion("str1", "", 12),
        base
      )
    }
  }
}
