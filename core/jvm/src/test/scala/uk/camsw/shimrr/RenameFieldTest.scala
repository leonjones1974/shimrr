package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import uk.camsw.shimrr.dsl.MigrationDsl

class RenameFieldTest extends FreeSpec {

  sealed trait Version
  case class V1(name: String) extends Version
  case class V2(firstName: String, surname: String) extends Version

  "fields can be renamed" in {

    @migration
    val v1 = new MigrationDsl[V1] {
      'firstName -> ((v1: V1) => v1.name)
      'surname -> "Jones"
    }

    import syntax._
    import v1.exports._
    import context.scoped._

    V1("Leon").migrateTo[V2] shouldBe V2("Leon", "Jones")
  }

}
