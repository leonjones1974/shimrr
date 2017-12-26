package uk.camsw.shimrr

import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.instances._
import uk.camsw.shimrr.test.MigrationFreeSpec

class GenerativeTest extends MigrationFreeSpec {

  "Given a coproduct with globally defined migration rules" - {

    implicit val ctx = MigrationContext.global(
      defaults =
        'stringField1 ->> "STR1" ::
        'stringField2 ->> "STR2" ::
        'intField1 ->> -99 ::
        HNil
    )

    anyCanBeMigratedToAny[Version]

  }
}




