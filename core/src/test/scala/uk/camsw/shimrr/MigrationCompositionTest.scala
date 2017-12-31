package uk.camsw.shimrr

import org.scalatest.FreeSpec
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import org.scalatest.Matchers._

class MigrationCompositionTest extends FreeSpec {

  "where there exists" - {
    "migrations from A ~> B and B ~> C" - {
      "we can create a migration from A ~> C" in {

        import context.scoped._
        val noCtx = MigrationContext[NoFields](
          'stringField1 ->> "str1" :: HNil
        )

        implicit val str1Ctx = MigrationContext[Str1](
          'stringField2 ->> "str2" :: HNil
        )

        type F1 = noCtx.fieldDefaults.type
        type F2 = str1Ctx.fieldDefaults.type

        implicit val composed = noCtx ++ str1Ctx

        import syntax._
        NoFields().migrateTo[Str1] shouldBe Str1("str1")
        NoFields().migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
        Str1("str1").migrateTo[Str1Str2] shouldBe Str1Str2("str1", "str2")
      }
    }
  }
}
