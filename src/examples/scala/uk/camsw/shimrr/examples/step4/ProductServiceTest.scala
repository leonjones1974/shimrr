package uk.camsw.shimrr.examples.step4

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class ProductServiceTest extends FreeSpec {
  val repository = InMemoryRepository
  val service = ProductService(repository)

  "given a service wrapped around a hard-coded in memory repository of products" - {
    "allProduct should return all products contained therein" in {
      service.allProducts() should contain only (
        BicycleV4("Raleigh", "Grifter", 124.99f, discountPercentage = 10),
        BicycleV4("Diamond Back", "Grind 2018", 230f, discountPercentage = 10),
        BicycleV4("Mondraker", "Podium Carbon", 4395f, discountPercentage = 10),
        BicycleV4("Qu-Ax", "Penny Farthing Gentlemen's bike", 483f, discountPercentage = 10),
        BicycleV4("Brompton", "M1E", 855f, discountPercentage = 10)
      )
    }
  }
}
