package uk.camsw.shimrr

import org.scalacheck.Arbitrary
import shapeless.HNil
import uk.camsw.shimrr.macros.test.MigrationFreeSpec
import shapeless.syntax.singleton.mkSingletonOps

class MigrationCompositionGenTest extends MigrationFreeSpec {

  "Given composed rules" -{

    import uk.camsw.shimrr.context.scoped._

    val noFieldsCtx = MigrationContext[NoFields](
      'stringField1 ->> "str1" :: HNil
    )

    val str1 = MigrationContext[Str1](
      'stringField2 ->> "str2" :: HNil
    )

    val str2 = MigrationContext[Str1Str2](
      'intField1 ->> 25 :: HNil
    )

    implicit val pipelineStr1Int1 = MigrationContext[Str1Str2Int1]()
    implicit val pipelineStr1 = str1 ++ str2
    implicit val pipelineStr2 = str2
    implicit val pipelineNoFields = noFieldsCtx ++ pipelineStr1

    "Any product can be migrated using the composed rules" - {

      allInACanBeMigratedToB[Version, Str1Str2Int1]

    }

    "a heterogeneous list of products can be migrated using the composed rules" - {

      allAInHListCanBeMigratedToB[Version, Str1Str2Int1]

    }
  }
}
