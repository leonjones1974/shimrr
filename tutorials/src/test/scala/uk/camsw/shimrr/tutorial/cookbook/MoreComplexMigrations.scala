package uk.camsw.shimrr.tutorial.cookbook

//import org.scalatest.prop.GeneratorDrivenPropertyChecks
//import uk.camsw.shimrr.dsl.{PipelineDsl2, PipelineDsl3}
//import uk.camsw.shimrr.pipeline
//import uk.camsw.shimrr.test.MigrationFreeSpec
//import uk.camsw.shimrr.syntax._
//
//sealed trait Sex
//case object Male extends Sex
//case object Female extends Sex
//case object Unknown extends Sex
//
//sealed trait Customer
//case class CustomerV1(name: String, age: Int)
//case class CustomerV2(name: String, age: Int, sex: Sex)
//case class CustomerV3(name: String, age: Int, sex: Sex, address: Option[AddressV2])
//
//sealed trait Address
//case class AddressV1(lines: List[String], postcode: String)
//case class AddressV2(lines: List[String], postcode: String, rating: Float)
//
//object Address {
//
//  @pipeline
//  val pipeline = new PipelineDsl2[AddressV1, AddressV2] {
//    from[AddressV1] {
//      'rating -> 0f
//    }
//
//    from[AddressV2] {}
//  }
//}
//
//object Customer {
//
//
//  @pipeline
//  val pipeline = new PipelineDsl3[CustomerV1, CustomerV2, CustomerV3] {
//    from[CustomerV1] {
//      'sex -> Unknown
//    }
//
//    from[CustomerV2] {
//      'address -> Option.empty[AddressV2]
//    }
//
//    from[CustomerV3] {
////      'address -> ((v2: CustomerV3) => {
////        import Address.pipeline.exports._
////        v2.address.map(_.migrateTo[AddressV2])
////      })
//    }
//  }
//}
//
//
//
//
//class MoreComplexMigrations extends MigrationFreeSpec {
//
//  "given a more complex object graph" - {
//    "we can still migrate it" in {
//      GeneratorDrivenPropertyChecks.forAll((customer: Customer) => {
//        println("Doing it for customer")
//      })
//    }
//  }
//
//
//}
