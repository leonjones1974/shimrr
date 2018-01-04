package uk.camsw.shimrr.tutorial.usecase2

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

/**
 * You update your service test, diligently covering the lead time defaulting for existing products
 * as well as support for those that are already on version 2
 */
class ProductServiceTest extends FreeSpec {

  val repository = InMemoryRepository
  val service = ProductService(repository)

  "given a service wrapped around a hard-coded in memory repository of products" - {

    "allProduct should return all products contained therein" in {
      service.allProducts() should contain only (
        BicycleV2("Raleigh", "Grifter", 124.99f, leadTime = 7),
        BicycleV2("Diamond Back", "Grind 2018", 230f, leadTime = 7),
        BicycleV2("Mondraker", "Podium Carbon", 4395f, leadTime = 14))
    }
  }
}
