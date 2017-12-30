package uk.camsw.shimrr
import java.util.concurrent.atomic.AtomicInteger

import org.scalatest.FreeSpec
import syntax._
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps

class LazyFieldTest extends FreeSpec {

  "using a global context" - {
    import uk.camsw.shimrr.context.global._

    val counter = new AtomicInteger(0)

    def nextCount: () => Int = () => counter.incrementAndGet()

    implicit val ctx = MigrationContext(
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
    import uk.camsw.shimrr.context.scoped._

    "an arity0 function can be used to default multiple missing fields" - {
      val counter = new AtomicInteger(4)

      def nextCount: () => Int = () => counter.incrementAndGet()

      def str2Name: () => String = () => "str2"

      implicit val ctx = MigrationContext[Str1](
        'stringField2 ->> str2Name ::
          'intField1 ->> nextCount :: HNil
      )

      Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 5)
    }
  }
}
