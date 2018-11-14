package uk.camsw.shimrr

import org.scalatest.Matchers._
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import uk.camsw.shimrr.TestSupport._
import uk.camsw.shimrr.dsl.PipelineDsl4
import uk.camsw.shimrr.test.MigrationFreeSpec

class PipelineDslGenTest extends MigrationFreeSpec {

  "Given a valid pipeline" - {

    @pipeline
    val pipeline = new PipelineDsl4[NoFields, Str1, Str1Str2, Str1Str2Int1] {

      from[NoFields] {
        'stringField1 -> "str1"
      }

      from[Str1] {
        'stringField2 -> (() => "str2")
      }

      from[Str1Str2] {
        'intField1 -> ((_: Str1Str2) => 25)
      }

      from[Str1Str2Int1] {}

    }

    import syntax._
    import pipeline.exports._

    "any product can be migrated to the target product" - {

      allInACanBeMigratedToB[Version, Str1Str2Int1]

    }

    "a heterogeneous list of products can be migrated to the target product" - {

      allAInHListCanBeMigratedToB[Version, Str1Str2Int1]

    }

    "all migrations should default correctly" in {
      GeneratorDrivenPropertyChecks.forAll((in: Version) => {

        val out = in.migrateTo[Str1Str2Int1]
        in match {
          case _: NoFields => out shouldBe Str1Str2Int1("str1", "str2", 25)
          case x: Str1 => out shouldBe Str1Str2Int1(x.stringField1, "str2", 25)
          case x: Str1Str2 => out shouldBe Str1Str2Int1(x.stringField1, x.stringField2, 25)
          case x: Str1Str2Int1 => out shouldBe Str1Str2Int1(x.stringField1, x.stringField2, x.intField1)
        }
      })
    }
  }
}
