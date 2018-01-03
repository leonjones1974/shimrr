package uk.camsw.shimrr.macros

import _root_.org.scalatest.FreeSpec
import org.scalacheck.ScalacheckShapeless

object test {

  import scala.language.experimental.macros
  import scala.reflect.macros.blackbox

  trait MigrationFreeSpecOps extends ScalacheckShapeless {
    this: FreeSpec =>

    def allInACanBeMigratedToAnyInA[A]: Any = macro MigrationFreeSpecMacros.generateAnyToAny[A]

    def allInACanBeMigratedToB[A, B]: Any = macro MigrationFreeSpecMacros.generateAllToSpecific[A, B]

    def allAInHListCanBeMigratedToB[A, B]: Any = macro MigrationFreeSpecMacros.generateAllInHListToSpecific[A, B]

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
            val inObj = implicitly[_root_.org.scalacheck.Arbitrary[$inT]]
            _root_.org.scalatest.prop.GeneratorDrivenPropertyChecks.forAll((in: $inT) => {
              _root_.uk.camsw.shimrr.Migration.migrate[$inT, $outT](in)
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

    def generateAllToSpecific[A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context): c.Expr[Any] = {
      import c.universe._

      val coproduct = c.weakTypeOf[A]
      val productsT = coproduct.typeSymbol.asClass.knownDirectSubclasses.map(_.asType)
      val target = c.weakTypeOf[B]

      val tests = for {
        inT <- productsT
      } yield {
        q"""
         "Migrating from " + ${inT.toString} + "~>" + ${target.toString} in {
            val inObj = implicitly[_root_.org.scalacheck.Arbitrary[$inT]]
            _root_.org.scalatest.prop.GeneratorDrivenPropertyChecks.forAll((in: $inT) => {
              _root_.uk.camsw.shimrr.Migration.migrate[$inT, $target](in)
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

    def generateAllInHListToSpecific[A: c.WeakTypeTag, B: c.WeakTypeTag](c: blackbox.Context): c.Expr[Any] = {
      import c.universe._

      val coproduct = c.weakTypeOf[A]
      val productsT = coproduct.typeSymbol.asClass.knownDirectSubclasses.map(_.asType)
      val target = c.weakTypeOf[B]

      val tests = for {
        inT <- productsT
      } yield {
        q"""
         "Migrating from " + ${inT.toString} + "~>" + ${target.toString} in {
            val inObj = _root_.org.scalacheck.Arbitrary.arbitrary[$inT]
            val listIn = _root_.org.scalacheck.Gen.listOf(30, inObj)
            _root_.org.scalatest.prop.GeneratorDrivenPropertyChecks.forAll((in: List[$coproduct]) => {
              import _root_.uk.camsw.shimrr.syntax._
              in.migrateTo[$target]
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

}
