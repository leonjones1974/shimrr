package uk.camsw.shimrr

//class FunctorTest extends FreeSpec {
//
//  "can migrate any functor F[Versioned]" - {
//    "Some " in {
//      import cats.instances.option._
//      Option(Str1("str1")).migrateTo[NoFields] shouldBe Some(NoFields())
//    }
//
//    "None" in {
//      import cats.instances.option._
//      Option.empty[Str1].migrateTo[NoFields] shouldBe None
//    }
//
//    "List" in {
//      List(Str1("str1")).migrateTo[NoFields] shouldBe List(NoFields())
//    }
//
//    "Iterable" in {
//      Iterable(Str1("str1")).migrateTo[NoFields] shouldBe Iterable(NoFields())
//    }
//  }
//}
