package uk.camsw.shimrr.examples.step5

import shapeless.HNil


trait ProductService {

  def allProducts(): Iterable[BicycleV4]

}

object ProductService {

  def apply(productRepository: ProductRepository, discountService: DiscountService): ProductService = new ProductService {
    val repository: ProductRepository = productRepository

    /**
      * Having updated the tests to expect a discount of 5% for all those versions that don't have a persisted discount
      * you wonder how to update the default field rule to call your service
      *
      * You decide to just try replacing the literal with your function
      */
    override def allProducts(): Iterable[BicycleV4] = {
      import uk.camsw.shimrr.context.global._
      import shapeless.syntax.singleton.mkSingletonOps
      import uk.camsw.shimrr.syntax._

      //noinspection TypeAnnotation
      implicit val allToV4 = MigrationContext(
        defaults = 'discountPercentage ->> discountService.defaultDiscountPercentage :: HNil
      )

      // You run the test, expecting compilation failures but it passes
      // Field defaulters can be of the form:
      //    T (literal)
      //    () => T (lazy)
      //    (A) => T (mapped) - more about that later!
      repository.findAll().migrateTo[BicycleV4]
    }
  }
}
