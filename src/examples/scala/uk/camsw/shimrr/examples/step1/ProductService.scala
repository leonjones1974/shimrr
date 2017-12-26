package uk.camsw.shimrr.examples.step1

/**
  * This is your product service.  At the moment it feels like it's just going to delegate to
  * the repository, but longer term you suspect it will earn its keep
  */
trait ProductService {

  /**
    * @return all the products that Bicycles Only Ltd. offer for sale
    */
  def allProducts(): Iterable[Bicycle]

}
