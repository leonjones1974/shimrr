package uk.camsw.shimrr

import org.scalatest.FreeSpec
import uk.camsw.shimrr.TestSupport.{ Str1, Str1Str2 }
import uk.camsw.shimrr.dsl.{ PipelineDsl1, PipelineDsl2 }
import syntax._
import org.scalatest.Matchers._

class PipelineTest extends FreeSpec {

  "it should be possible" - {
    "to define a pipeline" - {
      "of only one version" in {
        @pipeline
        val pipeline = new PipelineDsl1[Str1] {
          from[Str1] {}
        }

        import pipeline.exports._
        Str1("str1").migrateTo[Str1] shouldBe Str1("str1")
      }

      "of only two versions" in {
        @pipeline
        val pipeline = new PipelineDsl2[Str1, Str1Str2] {
          from[Str1] {
            'stringField2 -> "str2"
          }
          from[Str1Str2] {}
        }
        import pipeline.exports._
        Str1("str1").migrateTo[Str1] shouldBe Str1("str1")
        Str1("str1").migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
      }

    }
  }

}
