package uk.camsw.shimrr.test

import org.scalacheck.ScalacheckShapeless
import org.scalatest.FreeSpec
import uk.camsw.shimrr.ReadRepair
import scala.language.experimental.macros

import scala.reflect.macros.blackbox

trait MigrationFreeSpecOps extends ScalacheckShapeless {
  this: FreeSpec =>

  def anyCanBeMigratedToAny[A <: ReadRepair]: Any = macro MigrationFreeSpecMacros.generateAnyToAny[A]
}

abstract class MigrationFreeSpec extends FreeSpec with MigrationFreeSpecOps

object MigrationFreeSpecMacros {

  def generateAnyToAny[A: c.WeakTypeTag](c: blackbox.Context): c.Expr[Any] = {
    import c.universe._

    val coproduct = c.weakTypeOf[A]
    val productsT = coproduct.typeSymbol.asClass.knownDirectSubclasses.map(_.asType)

    val tests = for {
      inT <- productsT
      outT <- productsT
    } yield {
      q"""
         "Migrating from " + ${inT.toString} + "~>" + ${outT.toString} in {
            val inObj = implicitly[org.scalacheck.Arbitrary[$inT]]
            org.scalatest.prop.GeneratorDrivenPropertyChecks.forAll((in: $inT) => {
              uk.camsw.shimrr.Migration.migrate[$inT, $outT](in)
            })
         }
       """
    }

    c.Expr(
      q"""
         ..$tests
       """
    )
  }

}
