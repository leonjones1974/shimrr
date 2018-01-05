package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import uk.camsw.shimrr.dsl.PipelineDsl2

class OptionalFieldsTest extends FreeSpec {

  sealed trait Version
  case class V1(name: String) extends Version
  case class V2(name: String, age: Option[Int]) extends Version

  "it is possible to default optional fields" in {
    @pipeline
    val pipeline = new PipelineDsl2[V1, V2] {
      from[V1] {
        'age -> Option.empty[Int]
      }

      from[V2] {}
    }

    import pipeline.exports._
    import syntax._
    V1("Fred").migrateTo[V2] shouldBe V2("Fred", None)
  }

}
