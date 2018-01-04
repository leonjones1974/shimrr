package uk.camsw.shimrr.tutorial.usecase1

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

/**
 * You realise this test feels suspiciously like the repository test
 * but write it anyway
 */
class ProductServiceTest extends FreeSpec {

  val repository = InMemoryRepository
  val service = ProductService(repository)

  "given a service wrapped around a hard-coded in memory repository of products" - {

    "allProduct should return all products contained therein" in {
      service.allProducts() should contain only (
        Bicycle("Raleigh", "Grifter", 124.99f),
        Bicycle("Diamond Back", "Grind 2018", 230f))
    }
  }
}
