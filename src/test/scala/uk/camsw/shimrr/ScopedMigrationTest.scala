package uk.camsw.shimrr

import java.io.File

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.{HList, HNil}
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.context.myMacro
import uk.camsw.shimrr.syntax._

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

    "scopes can be applied to lists containing instances of a given coproduct" in {
      import context.scoped._

      sealed trait Versioned
      case class V1() extends Versioned
      case class V1a() extends Versioned
      case class V2(name: String) extends Versioned
      case class V3(name: String, age: Int) extends Versioned

      implicit val v1Rules = MigrationContext[V1](
        'name ->> "Leon Jones" :: 'age ->> 25 :: HNil
      )

      implicit val v1aRules = MigrationContext[V1a](
        'name ->> "Fred Basset" :: 'age ->> 51 :: HNil
      )

      implicit val v2Rules = MigrationContext[V2](
        'age ->> 69 :: HNil
      )

      implicit val v3Rules = MigrationContext[V3]()

      val xs = List[Versioned](
        V1(),
        V1a(),
        V2("Nicky Hayden"),
        V3("Casey Stoner", 27)
      )

      xs.migrateTo[V3] shouldBe List(
        V3("Leon Jones", 25),
        V3("Fred Basset", 51),
        V3("Nicky Hayden", 69),
        V3("Casey Stoner", 27)
      )
    }

    "at the coproduct level" in {
      sealed trait Versioned {
        def name: String
      }

      case class V1(name: String) extends Versioned
      case class V2(name: String, lengthOfName: Int) extends Versioned

      import uk.camsw.shimrr.context.scoped._

      val deriveLength: Versioned => Int = _.name.length

      implicit val ctx = MigrationContext[Versioned](
        'lengthOfName ->> deriveLength :: HNil
      )

      val xs = List[Versioned](
        V1("Leon Jones"),
        V2("Nicky Hayden", 12)
      )

      xs.migrateTo[V2] shouldBe List(
        V2("Leon Jones", 10),
        V2("Nicky Hayden", 12)
      )
    }

    "blah " in {
      import uk.camsw.shimrr.context.scoped._

//      implicit val ctxHard = MigrationContext[MyFish]()
//      println(s"${ctxHard}")

      @myMacro val list: List[File] = List(new File("/tmp"))
      println(s"${ctx}")



      def useIt[A, F <: HList](a: A)(implicit ev: MigrationContext[A, F]): Unit = {
        println(s"got ev of: ${ev}")
      }


      useIt(new MyFish{})
      useIt(new MyOtherFish{})
    }
  }
}

sealed trait MyFish
sealed trait MyOtherFish
