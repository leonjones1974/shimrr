package uk.camsw.shimrr

import cats.Monoid
import org.scalacheck.{Arbitrary, ScalacheckShapeless}
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

class Test

class GenerativeTest extends FreeSpec
  with MigrationContext
  with GenMigrationRules
  with ScalacheckShapeless {

  def migrationTest[T]: Any = macro QuasiquotesGenerator.generateTest[T]

  import syntax._

  "Given a set of coproducts" - {
    import uk.camsw.shimmr.Macros.Url._
    import cats.instances.int._


    //    "migrate" in {
    migrationTest[Version]
    //    }
  }

  override protected def fieldDefaults = fieldDefaultRules
}




