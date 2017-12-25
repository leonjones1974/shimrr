package uk.camsw.shimrr

import shapeless.HNil
import uk.camsw.shimrr.test.MigrationFreeSpec
import shapeless.syntax.singleton.mkSingletonOps

object GenerativeTestRules {

  private[shimrr] val globalFieldDefaults =
    'stringField1 ->> "STR1" ::
      'stringField2 ->> "STR2" ::
      'intField1 ->> -99 ::
      HNil

}

class GenerativeTest extends MigrationFreeSpec[GenerativeTestRules.globalFieldDefaults.type] {

  "Given a coproduct with globally defined migration rules" - {

    anyCanBeMigratedToAny[Version]

  }

  override val fieldDefaults = GenerativeTestRules.globalFieldDefaults
}




