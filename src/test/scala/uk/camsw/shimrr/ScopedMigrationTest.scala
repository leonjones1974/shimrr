package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.syntax._
import cats.instances.all._
import uk.camsw.shimrr.context.MigrationContext

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
      import uk.camsw.shimrr.context.global._
      {

        implicit val ctx = MigrationContext(
          defaults = 'age ->> 25 :: HNil
        )
        CustomerV1("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 25)
      }
      {
        implicit val ctx = MigrationContext(
          defaults = 'age ->> 51 :: HNil
        )
        CustomerV1("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 51)
      }
    }

    "for heterogeneous list - non-typesafe" in {
      import uk.camsw.shimrr.context.global._
      List[Entity](
        CustomerV1("cust1"),
        SupplierV1("supp1")
      ).collect {
        case c: Customer =>
          implicit val ctx = MigrationContext('age ->> 25 :: HNil)
          c.migrateTo[CustomerV2]
        case s: Supplier =>
          implicit val ctx = MigrationContext('age ->> 1 :: HNil)
          s.migrateTo[SupplierV2]
      } should contain only(CustomerV2("cust1", 25), SupplierV2("supp1", 1))
    }

    "for individual migrations (atom)" in {
      import uk.camsw.shimrr.context.scoped._
      implicit val V1toV2 = MigrationContext[CustomerV1]('age ->> 25 :: HNil)
      implicit val V1DupToV2 = MigrationContext[CustomerV1Dup]('age ->> 51 :: HNil)

      CustomerV1("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 25)
      CustomerV1Dup("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 51)
    }

    "for individual migrations with more than one field missing" in {
      import uk.camsw.shimrr.context.scoped._
      implicit val str1 = MigrationContext[Str1](
        'stringField2 ->> "str2" ::
          'intField1 ->> 0 :: HNil
      )

      Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 0)
    }

    "for individual migrations (list)" in {
      import uk.camsw.shimrr.context.scoped._
      implicit val V1toV2 = MigrationContext[CustomerV1]('age ->> 25 :: 'shoeSize ->> 7.5d :: HNil)
      implicit val V1DupToV2 = MigrationContext[CustomerV1Dup]('age ->> 51 :: 'shoeSize ->> 4.5d :: HNil)

      //TODO: Put me back in
      //      val xs = List[Customer](
      //        CustomerV1("name"),
      //        CustomerV1Dup("name")
      //      ).migrateTo[CustomerV3]
      //
      //
      //      xs shouldBe List(
      //        CustomerV3("name", 25, 7.5d),
      //        CustomerV3("name", 51, 7.5d)
      //      )
      fail("fix me, or move the one from parameterised lazy fields - this doesn't work when you have more than one field!")
    }

    // TODO: could do with implementing this at some stage
    "for heterogeneous list" ignore {
    }

  }
}
