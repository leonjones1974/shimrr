package uk.camsw.shimrr

import java.util.concurrent.atomic.AtomicInteger

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import uk.camsw.shimrr.TestSupport._
import uk.camsw.shimrr.dsl.MigrationDsl
import uk.camsw.shimrr.syntax._
import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import uk.camsw.shimrr.context.scoped._
import uk.camsw.shimrr.dsl.MigrationDsl
import uk.camsw.shimrr.syntax._

class MigrationDslTest extends FreeSpec {

  "The dsl" - {
    "can be used to define" - {

      "a migration using arrow style tuples for field defaults" in {
        @migration
        val str1Rules = new MigrationDsl[Str1] {

          'stringField2 -> "str2"

          'intField1 -> 25
        }
        import str1Rules.exports._

        Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)
      }

      "a migration using standard style tuple for field defaults" in {

        @migration
        val rules = new MigrationDsl[Str1Str2] {

          ('intField1, 51)

        }
        import rules.exports._

        Str1Str2("str1", "str2").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 51)
      }

      "multiple rules at the product level" in {
        @migration
        val rules = new MigrationDsl[Str1] {

          ('stringField2, "str2")
          ('intField1, 51)

        }
        import rules.exports._
        Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 51)
      }

      "migrations that require no field defaulters" in {
        @migration
        val rules = new MigrationDsl[Str1] {

        }
        import rules.exports._
        Str1("str1").migrateTo[Str1] shouldBe Str1("str1")
      }

      "multiple migrations at the product level" in {
        @migration
        val str1Rules = new MigrationDsl[Str1] {

          'stringField2 -> "str2"

          'intField1 -> 25
        }

        @migration
        val str1Str2Rules = new MigrationDsl[Str1Str2] {

          'intField1 -> 51
        }
        import str1Rules.exports._
        import str1Str2Rules.exports._

        Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)
        Str1Str2("str1", "str2").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 51)
      }

      "single migration at the coproduct level" in {
        @migration
        val allRules = new MigrationDsl[Version] {

          'stringField1 -> "str1"

          'stringField2 -> "str2"

          'intField1 -> 25
        }
        import allRules.exports._

        val xs = List[Version](
          NoFields(),
          Str1("str1"),
          Str1Str2("str1", "str2"),
          Str1Str2Int1("str1", "str2", 25)).migrateTo[Str1Str2Int1]

        xs shouldBe List.fill(4)(
          Str1Str2Int1("str1", "str2", 25))
      }

      // This needs to be declared outside the test
      object func {
        val counter = new AtomicInteger(0)

        val str2F = () => "str2"
      }

      "a migration containing literal, inlined lazy and parameterized field defaulters" in {

        @migration
        val rules = new MigrationDsl[NoFields] {
          ('stringField1, "str1")
          ('stringField2, (from: NoFields) => from.getClass.getSimpleName)
          ('intField1, func.counter.incrementAndGet _)
        }

        import rules.exports._
        NoFields().migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "NoFields", 1)
        NoFields().migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "NoFields", 2)
      }

    }
  }
}
