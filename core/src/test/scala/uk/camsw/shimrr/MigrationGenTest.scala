package uk.camsw.shimrr

import org.scalatest.Matchers._
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.test.MigrationFreeSpec

class MigrationGenTest extends MigrationFreeSpec {

  "Given a coproduct with globally complete migration rules" - {
    import uk.camsw.shimrr.context.global._

    implicit val ctx = MigrationContext(
      defaults =
        'stringField1 ->> "STR1" ::
          'stringField2 ->> "STR2" ::
          'intField1 ->> -99 ::
          HNil)

    "any product can be migrated to any other product" - {
      allInACanBeMigratedToAnyInA[Version]
    }

    "all migrations should default correctly" in {
      GeneratorDrivenPropertyChecks.forAll((in: Version) => {
        import syntax._

        val out = in.migrateTo[Str1Str2Int1]
        in match {
          case _: NoFields => out shouldBe Str1Str2Int1("STR1", "STR2", -99)
          case x: Str1 => out shouldBe Str1Str2Int1(x.stringField1, "STR2", -99)
          case x: Str1Str2 => out shouldBe Str1Str2Int1(x.stringField1, x.stringField2, -99)
          case x: Str1Str2Int1 => out shouldBe Str1Str2Int1(x.stringField1, x.stringField2, x.intField1)
        }
      })
    }
  }
}

