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
      * a sealed trait (coproduct).  Fortunately that's exactly how you modelled the Bicycle's so it sounds like a good fit!
      *
      * You look at the different representations and decide the following rule will suffice
      *  - When migrating from any bicycle 'x' to V3
      *  - If 'x' contains leadTime, drop it
      *
      * For this use-case there is no need to migrate V1 via V2, so it sounds like the default field dropping
      * behaviour will be just fine
      *
      */
    override def allProducts(): Iterable[BicycleV3] = {
      // Because you are applying rules globally you need to import 'everything' from the global context
      import uk.camsw.shimrr.context.global._

      // and the syntax, in order to get your extension method for iterable
      import uk.camsw.shimrr.syntax._

      // Your iterable now has a migrateTo extension method which can be used to make your tests pass
      repository.findAll().migrateTo[BicycleV3]
    }

  }
}
