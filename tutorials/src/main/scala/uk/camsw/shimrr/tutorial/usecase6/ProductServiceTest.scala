package uk.camsw.shimrr.tutorial.usecase6

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class ProductServiceTest extends FreeSpec {

  val repository = InMemoryRepository
  val service = ProductService(repository, DiscountService(defaultDiscountPercentage = 5))

  "given a service wrapped around a hard-coded in memory repository of products" - {
    "allProduct should return all products contained therein" in {
      service.allProducts() should contain only (
        BicycleV4("Raleigh", "Grifter", 124.99f, discountPercentage = 5),
        BicycleV4("Diamond Back", "Grind 2018", 230f, discountPercentage = 5),
        BicycleV4("Mondraker", "Podium Carbon", 4395f, discountPercentage = 5),
        BicycleV4("Qu-Ax", "Penny Farthing Gentlemen's bike", 483f, discountPercentage = 50),
        BicycleV4("Brompton", "M1E", 855f, discountPercentage = 10))
    }
  }
}
