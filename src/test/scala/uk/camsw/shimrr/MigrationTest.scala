package uk.camsw.shimrr

import cats.instances.int._
import cats.instances.string._
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import shapeless.{::, HList, HNil, Lazy}
import shapeless.labelled.{FieldType, field}
import shapeless.ops.record.Selector
import uk.camsw.shimrr.Migration._

object Dsl {

  import shapeless.syntax.singleton.mkSingletonOps
  val funs2 =
    (
      'stringField1 ->> {
        "STR1"
      }) :: (
      'stringField2 ->> {
        "STR2"
      }) :: (
      'intField1 ->> {
        -99
      }) :: HNil

}

class MigrationTest extends WordSpec {
  import Dsl._



  implicit def recordDefaulter[K <: Symbol, H, T <: HList](
                                                            implicit
                                                            mT: Lazy[Defaulter[T]],
                                                            selector: Lazy[Selector.Aux[funs2.type, K, H]]
                                                          ): Defaulter[FieldType[K, H] :: T] = {


    new Defaulter[FieldType[K, H] :: T] {
      val empty = {
        field[K](selector.value(funs2)) :: field[K](mT.value.empty)
      }
    }
  }

  val base = BaseVersion()

  "product.migrateTo" should {

    "map matching fields to V+1" in {
      val migrated = base.migrateTo[BaseVersion]
      migrated shouldBe base
    }

    "drop removed fields - variation 1" in {
      base.migrateTo[VersionWithoutStringField1] shouldBe base.withoutStringField1
    }

    "drop removed fields - variation 2" in {
      base.migrateTo[VersionWithoutStringField2] shouldBe base.withoutStringField2
    }

    "drop removed fields - variation 3" in {
      base.migrateTo[VersionWithNoFields] shouldBe base.withNoFields
    }

    "move fields" in {
      base.migrateTo[VersionWithSwappedFields] shouldBe base.withSwappedFields
    }

    "add missing fields - using monoid" in {
      VersionWithoutStringField1("str2", 32).migrateTo[BaseVersion] shouldBe BaseVersion("STR1", "str2", 32)
      VersionWithOnlyIntField(32).migrateTo[BaseVersion] shouldBe BaseVersion("STR1", "STR2", 32)
      VersionWithNoFields.migrateTo[BaseVersion] shouldBe BaseVersion("STR1", "STR2", -99)
      VersionWithOnlyIntField(15).migrateTo[VersionWithoutStringField1] shouldBe VersionWithoutStringField1("STR2", 15)
      VersionWithStringField1("str1").migrateTo[VersionWithOnlyIntField] shouldBe VersionWithOnlyIntField(-99)
    }
  }

  "coproduct.migrateTo" should {
    val x: Version = base

    "drop removed fields - list" in {
      val xs: List[Version] = List(base, base.withoutStringField1)
      xs.migrateTo[VersionWithNoFields] shouldBe List(VersionWithNoFields(), VersionWithNoFields())
    }

    "map fields - atom" in {
      x.migrateTo[BaseVersion] shouldBe base
    }

    "drop removed fields - atom" in {
      x.migrateTo[VersionWithoutStringField1] shouldBe base.withoutStringField1
    }

    "move fields - atom" in {
      x.migrateTo[VersionWithSwappedFields] shouldBe base.withSwappedFields
    }

    "move fields - list" in {
      val xs: List[Version] = List(base, base)
      xs.migrateTo[VersionWithSwappedFields] shouldBe List(base.withSwappedFields, base.withSwappedFields)
    }

    "add missing fields - atom - using monoid" in {
      val x: Version = VersionWithStringField1("str1")
      x.migrateTo[BaseVersion] shouldBe BaseVersion("str1", "STR2", -99)
    }

    "add missing fields - list - using monoid" in {
      val xs: List[Version] = List(VersionWithStringField1("str1"), VersionWithOnlyIntField(10), VersionWithNoFields(), base)
      xs.migrateTo[BaseVersion] shouldBe List(
        BaseVersion("str1", "STR2", -99),
        BaseVersion("STR1", "STR2", 10),
        BaseVersion("STR1", "STR2", -99),
        base
      )
    }

  }
}
