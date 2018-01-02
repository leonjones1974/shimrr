package uk.camsw.shimrr

import uk.camsw.shimrr.macros.pipeline
import uk.camsw.shimrr.macros.test.MigrationFreeSpec


class DslPipelineGenTest extends MigrationFreeSpec {

  "Given a valid pipeline" - {

    @pipeline
    val pipeline = new PipelineDsl[NoFields, Str1, Str1Str2, Str1Str2Int1] {

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

    import pipeline.exports._
    import uk.camsw.shimrr.context.scoped._
    import syntax._

    "any product can be migrated to the target product" - {

      allInACanBeMigratedToB[Version, Str1Str2Int1]

    }

    "a heterogeneous list of products can be migrated to the target product" - {

      allAInHListCanBeMigratedToB[Version, Str1Str2Int1]

    }
  }
}
