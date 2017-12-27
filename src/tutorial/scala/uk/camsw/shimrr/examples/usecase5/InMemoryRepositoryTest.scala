package uk.camsw.shimrr.examples.usecase5

import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class InMemoryRepositoryTest extends FreeSpec {

  "given a hard-coded in memory repository of products" - {
    "find all should return all products" in {
      InMemoryRepository.findAll() should contain only(
        BicycleV1("Raleigh", "Grifter", 124.99f),
        BicycleV1("Diamond Back", "Grind 2018", 230f),
        BicycleV2("Mondraker", "Podium Carbon", 4395f, leadTime = 14),
        BicycleV3("Qu-Ax", "Penny Farthing Gentlemen's bike", 483f),
        BicycleV4("Brompton", "M1E", 855f, discountPercentage = 10)
      )
    }
  }
}
