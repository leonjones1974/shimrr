package uk.camsw.shimrr

import shapeless.HNil
import uk.camsw.shimrr.test.MigrationFreeSpec
import shapeless.syntax.singleton.mkSingletonOps

trait GenerativeTestRules {

  private[shimrr] val globalFieldDefaults =
    'stringField1 ->> "STR1" ::
      'stringField2 ->> "STR2" ::
      'intField1 ->> -99 ::
      HNil

  // We must specify the type of our field defaulter
  type FIELD_DEFAULTS = globalFieldDefaults.type
}


class GenerativeTest extends MigrationFreeSpec
  with GenerativeTestRules {

  "Given a coproduct with globally defined migration rules" - {

    anyCanBeMigratedToAny[Version]

  }

  override val fieldDefaults: FIELD_DEFAULTS = globalFieldDefaults
}




