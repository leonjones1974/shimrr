package uk.camsw.shimrr

import org.scalatest.Matchers._
import uk.camsw.shimrr.dsl.{ PipelineDsl2, PipelineDsl3 }
import uk.camsw.shimrr.test.MigrationFreeSpec

class ExtendingPipelinesTest extends MigrationFreeSpec {

  sealed trait Version
  case class V1(name: String) extends Version
  case class V2(name: String, age: Int) extends Version
  case class V3(name: String, age: Int, dob: String) extends Version
  case class V4(name: String, age: Int, dob: String, postcode: String) extends Version

  @pipeline
  val pipeline1 = new PipelineDsl3[V1, V2, V3] {

    from[V1] {
      'age -> 25
    }

    from[V2] {
      'dob -> "01/09/1928"
    }

    from[V3] {}
  }

  @pipeline
  val pipeline2 = new PipelineDsl2[V3, V4] {
    from[V3] {
      'postcode -> "PO11 99R"
    }

    from[V4] {}

  }

  "given we have exceeded the 22 limit on a pipeline" - {

    "we can compose using scoping in" in {

      import syntax._

      def toV3(v1: V1): V3 = {
        import pipeline1.exports._
        v1.migrateTo[V3]
      }

      def toV4(v3: V3): V4 = {
        import pipeline2.exports._
        v3.migrateTo[V4]
      }

      toV4(toV3(V1("Leon"))) shouldBe V4("Leon", 25, "01/09/1928", "PO11 99R")
    }
  }
}
