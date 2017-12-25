package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.instances._
import uk.camsw.shimrr.syntax._

class ScopedMigrationTest extends FreeSpec {

  "Migration rules can be scoped" - {

    "like this" in {
      {
        implicit val ctx = MigrationContext(
          defaults = 'stringField1 ->> "str1" :: HNil
        )
        NoFields().migrateTo[Str1] shouldBe Str1("str1")
      }
      {
        implicit val ctx = MigrationContext(
          defaults =  'stringField2 ->> "str2" :: HNil
        )
        Str1("str1").migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
      }
    }
  }
}
