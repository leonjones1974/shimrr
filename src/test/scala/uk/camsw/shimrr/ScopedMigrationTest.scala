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
    //
    //    "manually, using blocks and global migration context" in {
    //      import uk.camsw.shimrr.context.global._
    //      {
    //
    //        implicit val ctx = MigrationContext(
    //          defaults = 'age ->> 25 :: HNil
    //        )
    //        CustomerV1("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 25)
    //      }
    //      {
    //        implicit val ctx = MigrationContext(
    //          defaults = 'age ->> 51 :: HNil
    //        )
    //        CustomerV1("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 51)
    //      }
    //    }
    //
    //    "for heterogeneous list - non-typesafe" in {
    //      import uk.camsw.shimrr.context.global._
    //      List[Entity](
    //        CustomerV1("cust1"),
    //        SupplierV1("supp1")
    //      ).collect {
    //        case c: Customer =>
    //          implicit val ctx = MigrationContext('age ->> 25 :: HNil)
    //          c.migrateTo[CustomerV2]
    //        case s: Supplier =>
    //          implicit val ctx = MigrationContext('age ->> 1 :: HNil)
    //          s.migrateTo[SupplierV2]
    //      } should contain only(CustomerV2("cust1", 25), SupplierV2("supp1", 1))
    //    }
    //
    //    "for individual migrations (atom)" in {
    //      import uk.camsw.shimrr.context.scoped._
    //      implicit val V1toV2 = MigrationContext[CustomerV1]('age ->> 25 :: HNil)
    //      implicit val V1DupToV2 = MigrationContext[CustomerV1Dup]('age ->> 51 :: HNil)
    //
    //      CustomerV1("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 25)
    //      CustomerV1Dup("name").migrateTo[CustomerV2] shouldBe CustomerV2("name", 51)
    //    }
    //
    //    "for individual migrations with more than one field missing" in {
    //      import uk.camsw.shimrr.context.scoped._
    //      implicit val str1 = MigrationContext[Str1](
    //        'stringField2 ->> "str2" ::
    //          'intField1 ->> 0 :: HNil
    //      )
    //
    //      Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 0)
    //    }
    //
    "scopes can be applied to heterogeneous lists" in {
      import context.scoped._

      sealed trait Scope
      case class V1() extends Scope
      case class V1a() extends Scope
      case class V2(name: String) extends Scope
      case class V3(name: String, age: Int) extends Scope

      implicit val v1Rules = MigrationContext[Scope, V1](
        'name ->> "Leon Jones" :: 'age ->> 51 :: HNil
      )

      implicit val v1aRules = MigrationContext[Scope, V1a](
        'name ->> "Bony" :: 'age ->> 25 :: HNil
      )

      implicit val v2Rules = MigrationContext[Scope, V2](
        'age ->> 77 :: HNil
      )

      implicit val v3Rules = MigrationContext[Scope, V3]()


      val xs = List[Scope](
        V1(),
        V1a(),
        V2("fish face"),
        V3("bloody hell", 99)
      )

      xs.migrateTo[V3] shouldBe List(
        V3("Leon Jones", 51),
        V3("Bony", 25),
        V3("fish face", 77),
        V3("bloody hell", 99)
      )
    }
  }
}
