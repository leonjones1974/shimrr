package uk.camsw.shimrr.tutorial.usecase3

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

/**
  * You update your service test to match the new requirement and, as expected, it fails:
  *  - V1 and V2 bicycles are still being migrated to V2
  *  - The V3 bicycle is simply being dropped
  */
class ProductServiceTest extends FreeSpec {

  val repository = InMemoryRepository
  val service = ProductService(repository)

  "given a service wrapped around a hard-coded in memory repository of products" - {
    "allProduct should return all products contained therein" in {
      service.allProducts() should contain only (
        BicycleV3("Raleigh", "Grifter", 124.99f),
        BicycleV3("Diamond Back", "Grind 2018", 230f),
        BicycleV3("Mondraker", "Podium Carbon", 4395f),
        BicycleV3("Qu-Ax", "Penny Farthing Gentlemen's bike", 483f)
      )
    }
  }
}
