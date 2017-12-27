package uk.camsw.shimrr.examples.usecase1

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

/**
  * You decide to write a test for your hard-coded map
  * Its of debatable value but your CTO wasn't available to pair with so you
  * erred on the side of caution and wrote it anyway
  */
class InMemoryRepositoryTest extends FreeSpec {

  "given a hard-coded in memory repository of products" - {
    "find all should return all products" in {
      InMemoryRepository.findAll() should contain only (
        Bicycle("Raleigh", "Grifter", 124.99f),
        Bicycle("Diamond Back", "Grind 2018", 230f)
      )
    }
  }
}
