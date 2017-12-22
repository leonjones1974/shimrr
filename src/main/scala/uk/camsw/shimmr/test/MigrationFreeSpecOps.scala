package uk.camsw.shimmr.test

import org.scalacheck.ScalacheckShapeless
import org.scalatest.FreeSpec
import uk.camsw.shimmr.QuasiquotesGenerator
import uk.camsw.shimrr.MigrationContext
import scala.language.experimental.macros

trait MigrationFreeSpecOps extends MigrationContext with ScalacheckShapeless {
  this: FreeSpec =>

  def anyCanBeMigratedToAny[T]: Any = macro QuasiquotesGenerator.generateTest[T]
}

abstract class MigrationFreeSpec extends FreeSpec with MigrationFreeSpecOps
