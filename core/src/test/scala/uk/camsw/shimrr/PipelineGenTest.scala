package uk.camsw.shimrr

import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.dsl.PipelineBuilder
import uk.camsw.shimrr.test.MigrationFreeSpec

class PipelineGenTest extends MigrationFreeSpec {

  "Given a coproduct with globally complete migration rules" - {

    "any product can be migrated to any other product" - {
      import uk.camsw.shimrr.context.global._

      implicit val ctx = MigrationContext(
        defaults =
          'stringField1 ->> "STR1" ::
            'stringField2 ->> "STR2" ::
            'intField1 ->> -99 ::
            HNil)

      allInACanBeMigratedToAnyInA[Version]
    }
  }

  "Given a valid pipeline" - {
    import uk.camsw.shimrr.context.scoped._

    implicit val noFieldsCtx = MigrationContext[NoFields](
      'stringField1 ->> "str1" :: HNil)

    implicit val str1 = MigrationContext[Str1](
      'stringField2 ->> ((_: Str1) => "str2") :: HNil)

    implicit val str2 = MigrationContext[Str1Str2](
      'intField1 ->> 25 :: HNil)

    implicit val int1 = MigrationContext[Str1Str2Int1]()

    implicit val (p1, p2) = PipelineBuilder[NoFields, Str1, Str1Str2, Str1Str2Int1].build

    "any product can be migrated to the target product" - {

      allInACanBeMigratedToB[Version, Str1Str2Int1]

    }

    "a heterogeneous list of products can be migrated to the target product" - {

      allAInHListCanBeMigratedToB[Version, Str1Str2Int1]

    }
  }

}

