package uk.camsw.shimrr.tutorial.usecase1

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

object ProductService {

  /**
    * You aren't a big fan of the cake pattern, so you opt for simple injection of your repository
    * implementation
    */
  def apply(productRepository: ProductRepository): ProductService = new ProductService {
    val repository:ProductRepository = productRepository

    override def allProducts(): Iterable[Bicycle] = repository.findAll()
  }
}
