package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.instances._
import uk.camsw.shimrr.syntax._
import cats.instances.all._

class ScopedMigrationTest extends FreeSpec {

  "Migration rules can be scoped" - {

    "manually" in {
      {
        implicit val ctx = MigrationContext(
          defaults = 'stringField1 ->> "str1" :: HNil
        )
        NoFields().migrateTo[Str1] shouldBe Str1("str1")
      }
      {
        implicit val ctx = MigrationContext(
          defaults = 'stringField2 ->> "str2" :: HNil
        )
        Str1("str1").migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
      }
    }

    sealed trait Entity extends ReadRepair
    sealed trait Customer extends Entity
    case class CustomerV1(name: String) extends Customer
    case class CustomerV1Dup(name: String) extends Customer
    case class CustomerV2(name: String, age: Int) extends Customer
    case class CustomerV3(name: String, age: Int, shoeSize: Double) extends Customer

    sealed trait Supplier extends Entity
    case class SupplierV1(companyName: String) extends Supplier
    case class SupplierV2(companyName: String, age: Int) extends Supplier

    "according to coproduct - non-typesafe" in {
      List[Entity](
        CustomerV1("cust1"),
        SupplierV1("supp1")
      ).migrateUnsafe {
        case c: Customer =>
          //noinspection TypeAnnotation
          implicit val ctx = MigrationContext('age ->> 25 :: HNil)
          c.migrateTo[CustomerV2]
        case s: Supplier =>
          //noinspection TypeAnnotation
          implicit val ctx = MigrationContext('age ->> 1 :: HNil)
          s.migrateTo[SupplierV2]
      } should contain only(CustomerV2("cust1", 25), SupplierV2("supp1", 1))
    }

    "according to specific version (atom) - typesafe" in {
      implicit val ctxV1 = ScopedMigrationContext[CustomerV1]('age ->> 25 :: HNil)
      implicit val ctxV2 = ScopedMigrationContext[CustomerV1Dup]('age ->> 51 :: HNil)

      CustomerV1("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 25)
      CustomerV1Dup("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 51)
    }

    "according to specific version (list) - typesafe" in {
      implicit val ctxV1 = ScopedMigrationContext[CustomerV1]('age ->> 25 :: HNil)
      implicit val ctxV2 = ScopedMigrationContext[CustomerV1Dup]('age ->> 51 :: HNil)

      val xs = List[Customer](
        CustomerV1("name"),
        CustomerV1Dup("name")
      ).migrateTo[CustomerV2]

      xs shouldBe List(
        CustomerV2("name", 25),
        CustomerV2("name", 51)
      )

    }

    //    "according to coproduct - typesafe" in {
    //      implicit val ctxV1 = ScopedMigrationContext[Customer]('age ->> 25 :: HNil)
    //
    //      CustomerV1("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 25)
    //
    //    }
  }
}
