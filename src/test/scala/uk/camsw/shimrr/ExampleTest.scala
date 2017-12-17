package uk.camsw.shimrr

import org.scalatest.WordSpec
import shapeless.HNil
import org.scalatest.Matchers._


sealed trait Customer

case class CustomerV1(name: String) extends Customer

case class CustomerV2(name: String, age: Int) extends Customer

case class CustomerV3(age: Int, name: String) extends Customer

object ExampleMigrationRules {

  import shapeless.syntax.singleton.mkSingletonOps

  val DefaultAge = -99

  val fieldDefaulters =
    'name ->> "Leon" ::
      'age ->> DefaultAge ::
      HNil
}

class ExampleTest extends WordSpec with MigrationInstances {

  import ExampleMigrationRules._

  // We need to import the syntax to get our migrateTo extension
  import syntax._

  // We must specify the type of our field defaulter
  type DEFAULTERS = fieldDefaulters.type

  // And provide a stable instance of it
  val defaulters = fieldDefaulters


  "Given we have setup field defaulters, we can migrate from V1 to V2" in {
    CustomerV1("Leon").migrateTo[CustomerV2] shouldBe CustomerV2("Leon", DefaultAge)
  }

  "In the other direction, missing fields are dropped" in {
    CustomerV2("Leon", 43).migrateTo[CustomerV1] shouldBe CustomerV1("Leon")
  }

  "Re-ordered fields are aligned automatically" in {
    CustomerV2("Leon", 43).migrateTo[CustomerV3] shouldBe CustomerV3(43, "Leon")
  }

  "Migration rules compose" in {
    CustomerV1("Leon").migrateTo[CustomerV3] shouldBe CustomerV3(DefaultAge, "Leon")
  }

  "Co-products are supported (with a little type help)" in {
    val xs: List[Customer] = List(
      CustomerV1("Leon"),
      CustomerV2("Leon", 43),
      CustomerV3(43, "Leon")
    )

    xs.migrateTo[CustomerV3] shouldBe List(
      CustomerV3(DefaultAge, "Leon"),
      CustomerV3(43, "Leon"),
      CustomerV3(43, "Leon")
    )
  }

  "New fields with associated default, fail to compile" in {
    case class CustomerV4(name: String, postCode: String)
    assertTypeError("""CustomerV1("Leon").migrateTo[CustomerV4]""")
  }

}
