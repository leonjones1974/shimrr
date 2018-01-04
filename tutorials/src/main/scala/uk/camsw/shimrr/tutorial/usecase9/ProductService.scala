package uk.camsw.shimrr.tutorial.usecase9

import uk.camsw.shimrr.dsl.PipelineDsl6
import uk.camsw.shimrr.pipeline

trait ProductService {

  def allProducts(): Iterable[BicycleV6]

}

object ProductService {

  def apply(productRepository: ProductRepository, discountService: DiscountService): ProductService = new ProductService {
    val repository: ProductRepository = productRepository

    /**
     * So, you 's convert what you before to the dsl.
     * You notice that all references to shapeless have gone
     * You no longer need to import the scope
     * Everything looks a lot more readable
     *
     * You notice that the migration from bicycle V2 ~> V4
     * is a simple, free field drop.  You could remove it
     * from your pipeline completely and skip to V3
     * However, if you declare a stage in the pipeline, you must
     * define a migration from it (even if it's empty like this one)
     *
     * Funcs for parameterized defaults can be inlined (as below), or
     * reference something external.
     * Currently (at least) they cannot be defined within the body of the
     * dsl (it's too confusing from a macro expansion perspective)
     *
     * It compiles so it looks like we can rename fields as we'd hoped
     */
    override def allProducts(): Iterable[BicycleV6] = {

      // You declare the pipeline and aren't overly surprised to find there is a 22 limit on versions
      // ... more about that later
      // The types defined the order of the migration
      // (future versions may infer the ordering from the internal migration definitions - we'll see)
      // Don't forget the annotation!!
      @pipeline
      val pipeline = new PipelineDsl6[BicycleV1, BicycleV2, BicycleV3, BicycleV4, BicycleV5, BicycleV6] {

        from[BicycleV1] {
          'leadTime -> 7
        }

        from[BicycleV2] {}

        from[BicycleV3] {
          'discountPercentage -> ((b: BicycleV3) => discountService.discountFor(b.make, b.model))
        }

        from[BicycleV4] {
          'price -> ((b: BicycleV4) => BigDecimal(b.price.toString))
        }

        from[BicycleV5] {
          'retailPrice -> ((b: BicycleV5) => b.price)
        }

        from[BicycleV6] {}
      }

      // Now we can just import everything the pipeline exports
      // NOTE: Importing 2 pipelines into the same scope is not currently
      // supported
      import pipeline.exports._
      import uk.camsw.shimrr.syntax._

      repository.findAll().migrateTo[BicycleV6]
    }

  }
}