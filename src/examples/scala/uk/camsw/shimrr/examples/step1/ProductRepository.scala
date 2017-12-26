package uk.camsw.shimrr.examples.step1

/**
  * This is the repository you introduce
  * You plan for it to provide a non-leaky abstraction over your underlying data-store, forever
  * You did consider calling it BicycleRepository, but felt that it would make the rest of
  * the tutorial a little more difficult to follow!
  */
trait ProductRepository {

  /**
    * @return all the bicycles we sell
    */
  def findAll(): Iterable[Bicycle]

}
