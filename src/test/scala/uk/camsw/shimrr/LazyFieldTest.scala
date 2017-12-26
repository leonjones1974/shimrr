package uk.camsw.shimrr

import java.util.concurrent.atomic.AtomicInteger

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.syntax._
import instances._

class LazyFieldTest extends FreeSpec {

  "using a global context" - {
    val counter = new AtomicInteger(0)

    def nextCount: () => Int = () => counter.incrementAndGet()

    implicit val ctx = MigrationContext.global(
      'intField1 ->> nextCount :: HNil
    )

    "an arity0 function can be used to default a field" in {
      val v1 = Str1Str2("str1", "str2").migrateTo[Str1Str2Int1]
      val v2 = Str1Str2("str1", "str2").migrateTo[Str1Str2Int1]

      v1.intField1 shouldBe 1
      v2.intField1 shouldBe 2
    }
  }

  "using a scoped context" - {
    val counter = new AtomicInteger(0)

    def nextCount: () => Int = () => counter.incrementAndGet()

    implicit val ctx = MigrationContext.scoped[Str1Str2](
      'intField1 ->> nextCount :: HNil
    )

    "an arity0 function can be used to default a field" in {
      val v1 = Str1Str2("str1", "str2").migrateTo[Str1Str2Int1]
      val v2 = Str1Str2("str1", "str2").migrateTo[Str1Str2Int1]

      v1.intField1 shouldBe 1
      v2.intField1 shouldBe 2
    }
  }
}
