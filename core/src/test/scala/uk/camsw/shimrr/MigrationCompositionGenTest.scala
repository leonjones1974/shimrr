package uk.camsw.shimrr

import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.test._

class MigrationCompositionGenTest extends MigrationFreeSpec {

  "Given composed rules" - {

    import uk.camsw.shimrr.context.scoped._

    val noFieldsCtx = MigrationContext[NoFields](
      'stringField1 ->> "str1" :: HNil)

    val str1 = MigrationContext[Str1](
      'stringField2 ->> "str2" :: HNil)

    val str2 = MigrationContext[Str1Str2](
      'intField1 ->> 25 :: HNil)

    implicit val mStr1Int1 = MigrationContext[Str1Str2Int1]()
    implicit val mStr1 = str1 ++ str2
    implicit val mStr2 = str2
    implicit val mNoFields = noFieldsCtx ++ mStr1

    "Any product can be migrated using the composed rules" - {

      allInACanBeMigratedToB[Version, Str1Str2Int1]

    }

    "a heterogeneous list of products can be migrated using the composed rules" - {

      allAInHListCanBeMigratedToB[Version, Str1Str2Int1]

    }
  }
}
