package uk.camsw.shimrr

import org.scalatest.Matchers._
import org.scalatest.WordSpec
import uk.camsw.shimrr.Migration._

class MigrationTest extends WordSpec {

  "migrateTo" should {

    "map matching fields to V+1" in {
      val base = BaseVersion()
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
  }
}
