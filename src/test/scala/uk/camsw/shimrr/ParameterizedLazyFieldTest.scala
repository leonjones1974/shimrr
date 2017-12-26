package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.syntax._
import uk.camsw.shimrr.instances._

class ParameterizedLazyFieldTest extends FreeSpec {

  private val nextCount: NoFields => Int = _ => 0

  "function that accepts current version can be used to default field" in {
    implicit val ctx = MigrationContext.scoped[NoFields](
      'intField1 ->> 0 :: HNil
    )

    NoFields().migrateTo[Int1] shouldBe Int1(0)
  }
}