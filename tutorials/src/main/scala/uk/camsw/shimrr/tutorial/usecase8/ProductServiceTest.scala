package uk.camsw.shimrr.tutorial.usecase8

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class ProductServiceTest extends FreeSpec {

  val repository = InMemoryRepository
  val service = ProductService(repository, DiscountService(defaultDiscountPercentage = 5))

  "given a service wrapped around a hard-coded in memory repository of products" - {
    "allProduct should return all products contained therein" in {
      service.allProducts() should contain only(
        BicycleV5("Raleigh", "Grifter", BigDecimal("124.99"), discountPercentage = 5),
        BicycleV5("Diamond Back", "Grind 2018", BigDecimal(230), discountPercentage = 5),
        BicycleV5("Mondraker", "Podium Carbon", BigDecimal(4395), discountPercentage = 5),
        BicycleV5("Qu-Ax", "Penny Farthing Gentlemen's bike", BigDecimal(483), discountPercentage = 50),
        BicycleV5("Brompton", "M1E", BigDecimal(855), discountPercentage = 10)
      )
    }
  }
}
