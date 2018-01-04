package uk.camsw.shimrr.tutorial.usecase9

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class ProductServiceTest extends FreeSpec {

  val repository = InMemoryRepository
  val service = ProductService(repository, DiscountService(defaultDiscountPercentage = 5))

  "given a service wrapped around a hard-coded in memory repository of products" - {
    "allProduct should return all products contained therein" in {
      service.allProducts() should contain only (
        BicycleV6("Raleigh", "Grifter", BigDecimal("124.99"), discountPercentage = 5),
        BicycleV6("Diamond Back", "Grind 2018", BigDecimal(230), discountPercentage = 5),
        BicycleV6("Mondraker", "Podium Carbon", BigDecimal(4395), discountPercentage = 5),
        BicycleV6("Qu-Ax", "Penny Farthing Gentlemen's bike", BigDecimal(483), discountPercentage = 50),
        BicycleV6("Brompton", "M1E", retailPrice = BigDecimal(855), discountPercentage = 10))
    }
  }
}
