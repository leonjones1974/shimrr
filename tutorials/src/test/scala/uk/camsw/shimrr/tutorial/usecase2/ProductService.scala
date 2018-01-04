package uk.camsw.shimrr.tutorial.usecase2

trait ProductService {

  /**
   * When you look at the service interface returning Bicycle you shudder and imagine
   * consumers of the component doing pattern matches all over the place
   *
   * You update the service to only ever returns V2 bicycle representations
   */
  def allProducts(): Iterable[BicycleV2]

}

object ProductService {

  def apply(productRepository: ProductRepository): ProductService = new ProductService {
    val repository: ProductRepository = productRepository

    /**
     * You decide that here is the place to perform the migration from Version 1 to Version 2
     * and map the repository contents accordingly
     */
    override def allProducts(): Iterable[BicycleV2] = {
      repository.findAll()
        .collect {
          case BicycleV1(make, model, price) =>
            BicycleV2(make, model, price, leadTime = 7)
          case v2: BicycleV2 =>
            v2
        }
    }
  }
}
