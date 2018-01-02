package uk.camsw.shimrr

import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps

class MigrationCompositionTest extends FreeSpec {

  "where there exists" - {
    "migrations from A ~> B and B ~> C" - {
      "we can create a migration from A ~> C" in {

        import context.scoped._
        val noFieldsCtx = MigrationContext[NoFields](
          'stringField1 ->> "str1" :: HNil
        )

        implicit val str1Ctx = MigrationContext[Str1](
          'stringField2 ->> "str2" :: HNil
        )

        implicit val composed = noFieldsCtx ++ str1Ctx

        import syntax._
        NoFields().migrateTo[Str1] shouldBe Str1("str1")
        NoFields().migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
        Str1("str1").migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
      }

      "we can compose using coproduct level rules" in {
        import context.scoped._

        val coproductCtx = MigrationContext[Version](
          'stringField2 ->> "str2" :: HNil
        )

        implicit val noCtx = MigrationContext[NoFields](
          'stringField1 ->> "str1" :: HNil
        ) ++ coproductCtx

        import syntax._
        NoFields().migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
      }

      "we can compose at the coproduct level using parameterized rules" in {
        import context.scoped._


        val noFieldsCtx = MigrationContext[NoFields](
          'stringField1 ->> "str1" :: HNil
        )

        val str1Str2 = MigrationContext[Str1Str2](
          'intField1 ->> 25 :: HNil
        )

        implicit val coproductCtx = MigrationContext[Version](
          'stringField2 ->> ((v: Version) => v.getClass.getSimpleName)
            :: HNil
        ) ++ noFieldsCtx ++ str1Str2


        import syntax._

        NoFields().asInstanceOf[Version].migrateTo[Str1Str2] shouldBe Str1Str2("str1", "NoFields")
        Str1("str1").asInstanceOf[Version].migrateTo[Str1Str2] shouldBe Str1Str2("str1", "Str1")
        Str1Str2("str1", "str2").asInstanceOf[Version].migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)
      }

      "we can migrate via pipeline when list of coproducts at V1" in {

        import context.scoped._
        implicit val noFieldsCtx = MigrationContext[NoFields](
          'stringField1 ->> "str1" :: HNil
        )

        implicit val str1 = MigrationContext[Str1](
          'stringField2 ->> "str2" :: HNil
        )

        implicit val str1Str2 = MigrationContext[Str1Str2](
          'intField1 ->> 25 :: HNil
        )

        import syntax._

        def migrateUsing[A1, A2, A3, A4](a: A1)(
          implicit
          a1: Migration[A1, A2],
          a2: Migration[A2, A3],
          a3: Migration[A3, A4]
        ): A4 =
          a3.migrate(a2.migrate(a1.migrate(a)))

        val xs = List[NoFields](
          NoFields(),
          NoFields()
        ).map(a => migrateUsing[NoFields, Str1, Str1Str2, Str1Str2Int1](a))

        xs shouldBe List(
          Str1Str2Int1("str1", "str2", 25),
          Str1Str2Int1("str1", "str2", 25)
        )
      }
    }
  }
}
