package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class DslOpsTest extends FreeSpec {

  "a code block" -{
    "can be evaluated within a context" in {
      DslOps.evalWithinContext {
        println("Hello")
        17
      } shouldBe 17
    }
  }

}
