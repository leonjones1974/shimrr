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

    val x = c.weakTypeOf[A]
    val subClasses = x.typeSymbol.asClass.knownDirectSubclasses
    println(s"Found subclasses: ${subClasses}")
    val scTypes = subClasses.map(_.asType)
    println(s"With types: ${scTypes}")

    val tests = for {
      in <- scTypes
      out <- scTypes
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

    //    c.Expr(
    //      q"""{
    //         "Migrating" in {
    //         val x: $x = Str1Str2("str1", "str2")
    //
    //         val xs: List[$x] = List(
    //            Str1Str2("str1", "str2")
    //         )
    //         val z = xs.migrateTo[$inType]
    //         println("z is: " + z)
    //          true shouldBe false
    //        }
    //        }
    //      """
    //    )
  }

}
