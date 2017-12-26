package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import uk.camsw.shimrr.syntax._
import instances._

class FunctorTest extends FreeSpec {

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
      List(Str1("str1")).migrateTo[NoFields] shouldBe List(NoFields())
    }

    "Iterable" in {
      Iterable(Str1("str1")).migrateTo[NoFields] shouldBe Iterable(NoFields())
    }
  }
}
