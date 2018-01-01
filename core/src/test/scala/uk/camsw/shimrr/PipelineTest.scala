package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil

class PipelineTest extends FreeSpec {

  import context.scoped._
  import shapeless.syntax.singleton.mkSingletonOps


  "Using pipelines" - {
    "it is possible to migrate" - {
      "through specific versions" - {
        "A ~> B, B ~> C, C ~ D: D" in {
          import syntax._

          // I want to generate a migration from A -> C
          // where migrations exist for A -> B and B -> C
          // Can I compose?
          val noFieldsCtx = MigrationContext[NoFields](
            'stringField1 ->> "str1" :: HNil
          )

          val str1 = MigrationContext[Str1](
            'stringField2 ->> "str2" :: HNil
          )

          val str2 = MigrationContext[Str1Str2](
            'intField1 ->> 25 :: HNil
          )

          implicit val pipelineStr1Int1 = MigrationContext[Str1Str2Int1]()
          implicit val pipelineStr1 = str1 ++ str2
          implicit val pipelineStr2 = str2
          implicit val pipelineNF = noFieldsCtx ++ pipelineStr1

          // A -> C via B
          NoFields().migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")

          // And now from B
          Str1("str1").migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")

          // And now a larger pipeline (A, B, C, D)
          // from C
          Str1Str2("str1", "str2").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)

          // from B
          Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)
          //
          // Finally from A
          NoFields().migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)

          // Now as a h-list
          val xs = List[Version](
            Str1Str2("str1", "str2"),
            Str1("fromstr1"),
            NoFields()
          ).migrateTo[Str1Str2Int1]
          //
          xs shouldBe List(
            // from C
            Str1Str2Int1("str1", "str2", 25),
            // from B
            Str1Str2Int1("fromstr1", "str2", 25),
            // from A
            Str1Str2Int1("str1", "str2", 25),
          )
        }
      }
      "where each intermediate version is materialized" in {
        import syntax._

        // I want to generate a migration from A -> C
        // where migrations exist for A -> B and B -> C
        // Can I compose?
        implicit val noFieldsCtx = MigrationContext[NoFields](
          'stringField1 ->> "str1" :: HNil
        )

        // This test would fail due to incompatible types in the parameterized defaulter
        // were we not passing via intermediate representations
        implicit val str1 = MigrationContext[Str1](
          'stringField2 ->> ((s1: Str1) => "str2") :: HNil
        )

        implicit val str2 = MigrationContext[Str1Str2](
          'intField1 ->> 25 :: HNil
        )

        implicit val int1 = MigrationContext[Str1Str2Int1]()

        implicit val (p1, p2) = Pipeline[NoFields, Str1, Str1Str2]
          .to[Str1Str2Int1]
          .build

        NoFields().migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)

        // Lets try from C to D which should just be a normal migration
        Str1Str2("str1", "str2").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)

        // But B to D may be more problematic?
        Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)

        // And as a typed list
        val xs = List[NoFields](
          NoFields()
        ).migrateTo[Str1Str2Int1]

        xs shouldBe List(
          Str1Str2Int1("str1", "str2", 25)
        )

        // And as an h-list
        val ys = List[Version](
          NoFields(),
          Str1("str1"),
          Str1Str2("str1", "str2"),
          Str1Str2Int1("str1", "str2", 51)
        ).migrateTo[Str1Str2Int1]

        ys shouldBe List(
          Str1Str2Int1("str1", "str2", 25),
          Str1Str2Int1("str1", "str2", 25),
          Str1Str2Int1("str1", "str2", 25),
          Str1Str2Int1("str1", "str2", 51),
        )

      }
    }
  }
}
