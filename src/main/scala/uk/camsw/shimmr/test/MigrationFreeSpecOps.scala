package uk.camsw.shimmr.test

import org.scalacheck.ScalacheckShapeless
import org.scalatest.FreeSpec
import uk.camsw.shimrr.MigrationContext

trait MigrationFreeSpecOps extends MigrationContext with ScalacheckShapeless {
  this: FreeSpec =>
}

abstract class MigrationFreeSpec extends FreeSpec with MigrationFreeSpecOps
