package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.instances._
import uk.camsw.shimrr.syntax._
import cats.instances.all._

//todo: See how much we really need this different set of classes
class ScopedMigrationTest extends FreeSpec {

  sealed trait Entity

  sealed trait Customer extends Entity

  case class CustomerV1(name: String) extends Customer

  case class CustomerV1Dup(name: String) extends Customer

  case class CustomerV2(name: String, age: Int) extends Customer

  case class CustomerV3(name: String, age: Int, shoeSize: Double) extends Customer

  sealed trait Supplier extends Entity

  case class SupplierV1(companyName: String) extends Supplier

  case class SupplierV2(companyName: String, age: Int) extends Supplier

  "Migration rules can be scoped" - {

    "manually, using blocks and global migration context" in {
      {
        implicit val ctx = MigrationContext.global(
          defaults = 'age ->> 25 :: HNil
        )
        CustomerV1("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 25)
      }
      {
        implicit val ctx = MigrationContext.global(
          defaults = 'age ->> 51 :: HNil
        )
        CustomerV1("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 51)
      }
    }

    // TODO: could do with implementing this at some stage
    "for heterogeneous list" ignore {
    }

    "for heterogeneous list - non-typesafe" in {
      List[Entity](
        CustomerV1("cust1"),
        SupplierV1("supp1")
      ).collect {
        case c: Customer =>
          implicit val ctx = MigrationContext.global('age ->> 25 :: HNil)
          c.migrateTo[CustomerV2]
        case s: Supplier =>
          implicit val ctx = MigrationContext.global('age ->> 1 :: HNil)
          s.migrateTo[SupplierV2]
      } should contain only(CustomerV2("cust1", 25), SupplierV2("supp1", 1))
    }

    "for individual migrations (atom)" in {
      implicit val V1toV2 = MigrationContext.scoped[CustomerV1]('age ->> 25 :: HNil)
      implicit val V1DupToV2 = MigrationContext.scoped[CustomerV1Dup]('age ->> 51 :: HNil)

      CustomerV1("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 25)
      CustomerV1Dup("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 51)
    }

    "for individual migrations with more than one field missing" in {
      implicit val str1 = MigrationContext.scoped[Str1](
        'stringField2 ->> "str2" ::
          'intField1 ->> 0 :: HNil
      )

      Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 0)
    }

    "for individual migrations (list)" in {
      implicit val V1toV2 = MigrationContext.scoped[CustomerV1]('age ->> 25 :: HNil)
      implicit val V1DupToV2 = MigrationContext.scoped[CustomerV1Dup]('age ->> 51 :: HNil)

      val xs = List[Customer](
        CustomerV1("name"),
        CustomerV1Dup("name")
      ).migrateTo[CustomerV2]

      xs shouldBe List(
        CustomerV2("name", 25),
        CustomerV2("name", 51)
      )
    }
  }
}
