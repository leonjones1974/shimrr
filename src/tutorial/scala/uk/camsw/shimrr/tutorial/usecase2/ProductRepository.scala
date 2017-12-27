package uk.camsw.shimrr.tutorial.usecase2


/**
  * You decide that doing the version upgrade within the repository doesn't feel quite right
  * On that basis you decide it needs to return the more abstract representation of a bicycle
  */
trait ProductRepository {

  def findAll(): Iterable[Bicycle]

}


/**
  * Having abstracted the interface you can store and retrieved bicycles of different versions
  */
object InMemoryRepository extends ProductRepository {
  override def findAll(): Iterable[Bicycle] = List(
    BicycleV1("Raleigh", "Grifter", 124.99f),
    BicycleV1("Diamond Back", "Grind 2018", 230f),
    BicycleV2("Mondraker", "Podium Carbon", 4395f, leadTime = 14)
  )
}
