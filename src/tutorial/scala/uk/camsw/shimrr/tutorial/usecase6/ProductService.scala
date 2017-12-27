package uk.camsw.shimrr.tutorial.usecase6

import shapeless.HNil

trait ProductService {

  def allProducts(): Iterable[BicycleV4]

}

object ProductService {

  def apply(productRepository: ProductRepository, discountService: DiscountService): ProductService = new ProductService {
    val repository: ProductRepository = productRepository

    /**
      * Having looked at your model and deciding that in this case, conveniently, you can pull
      * make and model up to Bicycle you crack on with the new service implementation.
      *
      * Somewhere in the SHIMRR tutorial you read something about scoping and wonder whether you
      * can scope rules at the bicycle level.
      *
      * If you could combine that with a field defaulter that had access to the existing bicycle then
      * it might be possible to solve the problem
      */
    override def allProducts(): Iterable[BicycleV4] = {
      import shapeless.syntax.singleton.mkSingletonOps
      import uk.camsw.shimrr.syntax._

      // This time you want scoped rules, so pull in the scoped context
      import uk.camsw.shimrr.context.scoped._

      // In order to get the discount we are probably going to want a func from Bicycle => Int
      val discount = (b: Bicycle) => discountService.discountFor(b.make, b.model)

      /*
        Now lets create our migration context. Because we're in the scoped context we need to provide
        the type by which to scope in addition to defining our normal rules
        */
      implicit val ctx = MigrationContext[Bicycle](
        'discountPercentage ->> discount :: HNil
      )

      /**
        * With baited breath you recompile, it compiles and the tests pass
        * NOTE: The more observant may have, well, observed, may have wondered why the
        * global context exists when we can just scope by the trait.  In answer, that's
        * the joy of iterative development - it may be that by the time I'm writing use case 10
        * I become convinced of its redundancy.  Until then, you have the choice
        */
      repository.findAll().migrateTo[BicycleV4]
    }
  }
}
