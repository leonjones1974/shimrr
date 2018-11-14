package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import uk.camsw.shimrr.dsl.PipelineDsl3
import org.scalatest.FreeSpec
import uk.camsw.shimrr.dsl.PipelineDsl3
import org.scalatest.Matchers._

class NonBreakingChangesTest extends FreeSpec {

  sealed trait Version

  case class V1(name: String) extends Version

  case class V2(name: String, age: Int) extends Version

  case class V3(name: String, age: Int, surname: String) extends Version

  object Version {
    type Latest = V3

    @pipeline
    val pipeline = new PipelineDsl3[V1, V2, Latest] {
      from[V1] {
        'age -> 25
      }

      from[V2] {
        'surname -> "Jones"
      }

      from[Latest] {}
    }
  }

  "we can abstract ourselves from non breaking changes" - {
    "using type aliases" in {
      import Version.pipeline.exports._
      import syntax._
      V1("Leon").migrateTo[Version.Latest] shouldBe V3("Leon", 25, "Jones")
    }
  }
}
