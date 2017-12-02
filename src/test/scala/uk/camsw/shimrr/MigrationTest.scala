package uk.camsw.shimrr

import org.scalatest.Matchers._
import org.scalatest.WordSpec
import uk.camsw.shimrr.Migration._

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

    "move fields" in {
      base.migrateTo[VersionWithSwappedFields] shouldBe base.withSwappedFields
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

    "move fields" in {
      val xs: List[Version] = List(base, base)
      xs.migrateTo[VersionWithSwappedFields] shouldBe List(base.withSwappedFields, base.withSwappedFields)
    }
  }

}
