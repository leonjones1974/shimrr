package uk.camsw.shimrr

import cats.Monoid
import org.scalacheck.Arbitrary
import org.scalatest.FreeSpec
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import shapeless.{Generic, HList, HNil}
import uk.camsw.shimmr._
import uk.camsw.shimmr.Macros.evalImpl

import scala.language.experimental.macros
import scala.reflect.runtime.{universe => ru}


trait GenMigrationRules {

  import shapeless.syntax.singleton.mkSingletonOps

  private[shimrr] val DefaultAge = -99


  // Here we define our rules for defaulting fields.  Currently there must be an entry for every new field added since V1 and they apply globally across
  // all possible migrations for a given coproduct
  private[shimrr] val fieldDefaultRules =
  'stringField1 ->> "STR1" ::
    'stringField2 ->> "STR2" ::
    'intField1 ->> -99 ::
    HNil

  // We must specify the type of our field defaulter
  type FIELD_DEFAULTS = fieldDefaultRules.type
}

//
//@FromSchema("/fish/flop")
//class Foo

//@TestForType[Version]("fish")
class Test

class GenerativeTest extends FreeSpec with MigrationContext with GenMigrationRules {

//  val f = new Foo("hello")
//  println("my field: " + f.myField)

//  println(s"f: ${f}")
//  val x = new Test("fi")

  def eval(expr: Class[_]): Unit = macro evalImpl

  def evalT[T]: Monoid[T] = macro Macros.evalTImpl[T]

  def evalStr[T](expr: String): Any = macro Macros.evalStrImpl[T]

  def migrationTest[T]: Any = macro QuasiquotesGenerator.generateTest[T]

//  evalT[Version]

  import syntax._
  import org.scalacheck.ScalacheckShapeless._

  import org.scalatest.Matchers._
  "Given a set of coproducts" - {
    import uk.camsw.shimmr.Macros.Url._
    import cats.instances.int._

//    "migrate" in {
      migrationTest[Version]
//    }
  }

  override protected def fieldDefaults = fieldDefaultRules
}




