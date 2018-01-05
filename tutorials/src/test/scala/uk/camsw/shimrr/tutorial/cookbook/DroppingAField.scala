package uk.camsw.shimrr.tutorial.cookbook

import uk.camsw.shimrr.test.MigrationFreeSpec

/**
 * Note the use of the MigrationFreeSpec, this
 * gives us access to test generators
 */
class DroppingAField extends MigrationFreeSpec {

  "Given a simple model" - {
    "When I migrate to the next version" - {
      "Then removed fields are dropped automatically" in {
      }
    }
  }
}
