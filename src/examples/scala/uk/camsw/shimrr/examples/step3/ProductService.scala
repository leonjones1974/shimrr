package uk.camsw.shimrr.examples.step3

trait ProductService {

  /**
    * Again, you update the service to return the latest version
    */
  def allProducts(): Iterable[BicycleV3]

}

object ProductService {

  def apply(productRepository: ProductRepository): ProductService = new ProductService {
    val repository: ProductRepository = productRepository

    /**
      * This time you decide to use shimrr to perform the migration
      *
      * The documentation suggests that field dropping is 'free' providing your case classes (products) extend
      * a sealed trait (coproduct)
      *
      * Fortunately that's exactly how you modelled the Bicycle's so it sounds like a good fit
      * You look at the different representations and decide the following rule will suffice
      *  - When migrating from any bicycle 'A' to V3 'B'
      *  - If the A contains leadTime, drop it
      *
      * For this use-case there is no need to migrate V1 via V2 so it sounds like the default field dropping
      * behaviour will be just fun
      *
      * You worry a little about the imports, but figure you will have to get used to it if you want a type level
      * solution to your problem
      */
    override def allProducts(): Iterable[BicycleV3] = {
      // You import the syntax in order to get access to the migration extension methods
      import uk.camsw.shimrr.syntax._
      // And the shimrr type class instances
      import uk.camsw.shimrr.context.global._

      // You add the single liner and all your tests pass!
      repository.findAll().migrateTo[BicycleV3]
    }
  }
}
