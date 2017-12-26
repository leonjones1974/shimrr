package uk.camsw.shimrr.examples.step3

import shapeless.HNil
import uk.camsw.shimrr.MigrationContext
import uk.camsw.shimrr.examples.step2.{BicycleV1, BicycleV2}

trait ProductService {

  /**
    * Again, you update the service to return V3
    */
  def allProducts(): Iterable[BicycleV3]

}

object ProductService {

  def apply(productRepository: ProductRepository): ProductService = new ProductService {
    val repository: ProductRepository = productRepository

    /**
      * This time you decide to use shimrr to perform the migration
      * The documentation mentions a Global Migration Context,
      * explaining that it provides a global set of migration rules that apply to
      * all case classes that are children of a given sealed trait
      *
      * Fortunately that's exactly how you modelled the Bicycle's so it sounds like a good fit
      * You look at the different representations and decide the following rule will suffice
      *  - When migrating from any bicycle 'A' to V3 'B'
      *  - If the A contains leadTime, drop it
      *
      * For this use-case there is no need to migrate V1 via V2, so it sounds like global migration context
      * will suffice
      *
      * You worry a little about the imports, but figure you will have to get used to it if you want a type level
      * solution to your problem
      */
    override def allProducts(): Iterable[BicycleV3] = {
      // You import the syntax

      // And the shimrr type class instances required to perform the migration

      /* You've read in the documentation that you can migrate anything for which a cats.Functor can be summoned
       as you have a list, you decide to use that
       Initially you can't find the migrateTo extension method that the document refers to, then you realise
       you haven't got the cats functor type class instances in scope
       */
      import cats.instances.all._
      import cats.syntax.all._

      val bicycles: List[Bicycle] = repository.findAll().toList
      import uk.camsw.shimrr.instances._
      import uk.camsw.shimrr.syntax._
      implicit val ctx = MigrationContext.global()
      bicycles.migrateTo[BicycleV3]


      repository.findAll()
        .collect {
          case BicycleV1(make, model, price) =>
            BicycleV2(make, model, price, leadTime = 7)
          case v2: BicycleV2 =>
            v2
        }
    }
  }
}
