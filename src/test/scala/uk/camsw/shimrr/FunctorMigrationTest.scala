package uk.camsw.shimrr

import org.scalatest.FreeSpec
import shapeless.HNil
import org.scalatest.Matchers._
import syntax._

class FunctorMigrationTest extends FreeSpec
  with MigrationContext {

  type FIELD_DEFAULTS = HNil.type
  override val fieldDefaults: FIELD_DEFAULTS = HNil

  "can migrate any functor F[Versioned]" - {
    "Some " in {
      import cats.instances.option._
      Option(Str1("str1")).migrateTo[NoFields] shouldBe Some(NoFields())
    }

    "None" in {
      import cats.instances.option._
      Option.empty[Str1].migrateTo[NoFields] shouldBe None
    }

    "List" in {
      import cats.instances.list._
      List(Str1("str1")).migrateTo[NoFields] shouldBe List(NoFields())
    }
  }
}
