package uk.camsw.shimrr

import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.macros.test.MigrationFreeSpec

class MigrationGenTest extends MigrationFreeSpec {

  "Given a coproduct with globally complete migration rules" - {

    "any product can be migrated to any other product" - {
      import uk.camsw.shimrr.context.global._

      implicit val ctx = MigrationContext(
        defaults =
          'stringField1 ->> "STR1" ::
            'stringField2 ->> "STR2" ::
            'intField1 ->> -99 ::
            HNil
      )

      allInACanBeMigratedToAnyInA[Version]
    }
  }





}




