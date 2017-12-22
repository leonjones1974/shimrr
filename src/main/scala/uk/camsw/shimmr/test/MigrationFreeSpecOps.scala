package uk.camsw.shimmr.test

import org.scalacheck.ScalacheckShapeless
import org.scalatest.FreeSpec
import uk.camsw.shimmr.QuasiquotesGenerator
import uk.camsw.shimrr.MigrationContext

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

trait MigrationFreeSpecOps extends MigrationContext with ScalacheckShapeless {
  this: FreeSpec =>

  def anyCanBeMigratedToAny[T]: Any = macro MigrationTestMacros.generateTest[T]
}

abstract class MigrationFreeSpec extends FreeSpec with MigrationFreeSpecOps


object MigrationTestMacros {
  def generateTest[A: c.WeakTypeTag](c: blackbox.Context) = {
    import c.universe._
    println("I am in here")

    c.Expr(
      q"""
        case class AClass() {
        }
      """
    )


    // retrieve the schema path
    //    val schemaPath = c.prefix.tree match {
    //      case Apply(_, List(Literal(Constant(x)))) => x.toString
    //      case _ => c.abort(c.enclosingPosition, "schema file path is not specified")
    //    }
    //    println(s"Schema path is: ${schemaPath}")

    // retrieve the annotate class name
    //    val className = annottees.map(_.tree) match {
    //      case List(q"class $name") => name
    //      case _ => c.abort(c.enclosingPosition, "the annotation can only be used with classes")
    //    }
    //
    //    val imp = q"implicitly[Monoid[$className]]"

    //    println(s"implicit summonded: ${imp}")
    //
    //    println(s"className is: ${className}")


    val params = Seq(
      q"val myField: String"
    )
    //    // load the schema from JSON
    //    val schema = TypeSchema.fromJson(schemaPath)
    //
    //    // produce the list of constructor parameters (note the "val ..." syntax)
    //    val params = schema.fields.map { field =>
    //      val fieldName = newTermName(field.name)
    //      val fieldType = newTypeName(field.valueType.fullName)
    //      q"val $fieldName: $fieldType"
    //    }
    //
    //    val json = TypeSchema.toJson(schema)

    // rewrite the class definition
    //case class $className(..$params) {
    //          def schema = ${json}
    //$className(..$params)

    val x = c.weakTypeOf[A]
    val subClasses = x.typeSymbol.asClass.knownDirectSubclasses
    println(s"Found subclasses: ${subClasses}")
    val scTypes = subClasses.map(_.asType)
    println(s"With types: ${scTypes}")

    //          val m = implicitly[Monoid[$x]]


    /*
      println("Hello")

          val x: $x = Str1Str2("str1", "str2")
          println("x is" + x)
          val z = x.migrateTo[Str1]
          println("z is: " + z)

          println("Empty is: " + m.empty)
     */


    //
    val inType = scTypes.head
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
