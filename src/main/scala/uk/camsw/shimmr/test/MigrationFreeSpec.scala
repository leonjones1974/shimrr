package uk.camsw.shimmr.test

import org.scalacheck.ScalacheckShapeless
import org.scalatest.FreeSpec
import uk.camsw.shimrr.MigrationContext

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

trait MigrationFreeSpecOps extends MigrationContext with ScalacheckShapeless {
  this: FreeSpec =>

  def anyCanBeMigratedToAny[T]: Any = macro MigrationFreeSpecMacros.generateAnyToAny[T]
}

abstract class MigrationFreeSpec extends FreeSpec with MigrationFreeSpecOps


object MigrationFreeSpecMacros {

  def generateAnyToAny[A: c.WeakTypeTag](c: blackbox.Context): c.Expr[Any] = {
    import c.universe._

    val coproduct = c.weakTypeOf[A]
    val productsT = coproduct.typeSymbol.asClass.knownDirectSubclasses.map(_.asType)

    val tests = for {
      in <- productsT
      out <- productsT
    } yield {
      q"""
         "Migrating from " + ${in.toString} + "~>" + ${out.toString} in {
            val inObj = implicitly[org.scalacheck.Arbitrary[$in]]
            org.scalatest.prop.GeneratorDrivenPropertyChecks.forAll((from: $in) => {
              uk.camsw.shimrr.Migration.migrate[$in, $out](from)
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
