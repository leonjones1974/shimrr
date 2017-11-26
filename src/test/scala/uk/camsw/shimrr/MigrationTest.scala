package uk.camsw.shimrr

import org.scalatest.Matchers._
import org.scalatest.WordSpec
import uk.camsw.shimrr.Migration._

class MigrationTest extends WordSpec {
  val base = BaseVersion()

  "migrateTo" should {
    "map matching fields to V+1" in {
      val migrated = base.migrateTo[BaseVersion]
      migrated shouldBe base
    }

    "drop removed fields - variation 1" in {
      val base = BaseVersion()
      base.migrateTo[VersionWithoutStringField1] shouldBe base.withoutStringField1
    }

    "drop removed fields - variation 2" in {
      val base = BaseVersion()
      base.migrateTo[VersionWithoutStringField2] shouldBe base.withoutStringField2
    }

    "drop removed fields - variation 3" in {
      val base = BaseVersion()
      base.migrateTo[VersionWithNoFields] shouldBe base.withNoFields
    }

    "drop fields from co-products" in {
      val base: Version = BaseVersion()
      migrate[Version, VersionWithNoFields](base) shouldBe VersionWithNoFields()
    }
  }

  "genseq.migrateTo" should {
    "drop removed fields from co-products" in {
      val xs: List[Version] = List(base, base.withoutStringField1)
      xs.migrateTo[VersionWithNoFields] shouldBe List.fill(xs.size)(VersionWithNoFields())
    }
  }

}
