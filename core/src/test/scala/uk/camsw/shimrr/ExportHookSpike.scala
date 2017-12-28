package uk.camsw.shimrr

import org.scalatest.FreeSpec
import uk.camsw.shimrr.macros.migration
import org.scalatest.Matchers._

trait Rules[T] {
  def echo(t: T): T = t
}

@migration
object Str1Rules {
  type FROM = Str1
}

@migration
object Int1Rules {
  type FROM = Int1
}

class ExportHookSpike extends FreeSpec {

  "rules will have correct type" in {
    val x: Str1Rules.FROM = Str1("str1")
  }

  "contexts can be imported from the rules" in {
    import Str1Rules.exports._
    implicitly[Rules[Str1]].echo(Str1("boo")) shouldBe Str1("boo")
  }

  "contexts from multi rules do not conflict" in {
    import Int1Rules.exports._
    import Str1Rules.exports._
    implicitly[Rules[Int1]].echo(Int1(25)) shouldBe Int1(25)
    implicitly[Rules[Str1]].echo(Str1("boo")) shouldBe Str1("boo")
  }

}
