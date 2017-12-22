package uk.camsw.shimmr.test

import org.scalacheck.ScalacheckShapeless
import org.scalatest.FreeSpec
import uk.camsw.shimrr.MigrationContext

trait MigrationFreeSpec extends MigrationContext with ScalacheckShapeless {
  this: FreeSpec =>

}
