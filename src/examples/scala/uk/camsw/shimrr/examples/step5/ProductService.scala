package uk.camsw.shimrr.examples.step5

import shapeless.HNil
import uk.camsw.shimrr.context.MigrationContext

import shapeless.syntax.singleton.mkSingletonOps

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
      import uk.camsw.shimrr.syntax._
      import uk.camsw.shimrr.context.global._

      implicit val allToV4 = MigrationContext(
        defaults = 'discountPercentage ->> discountService.defaultDiscountPercentage :: HNil
      )
      // You run the test, expecting compilation failures
      // but it passes

      repository.findAll().migrateTo[BicycleV4]
    }
  }
}
