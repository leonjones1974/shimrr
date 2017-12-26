package uk.camsw.shimrr.examples.step4

trait ProductRepository {

  def findAll(): Iterable[Bicycle]

}

object InMemoryRepository extends ProductRepository {
  override def findAll(): Iterable[Bicycle] = List(
    BicycleV1("Raleigh", "Grifter", 124.99f),
    BicycleV1("Diamond Back", "Grind 2018", 230f),
    BicycleV2("Mondraker", "Podium Carbon", 4395f, leadTime = 14),
    BicycleV3("Qu-Ax", "Penny Farthing Gentlemen's bike", 483f),
    BicycleV4("Brompton", "M1E", 855f, discountPercentage = 10)
  )
}
