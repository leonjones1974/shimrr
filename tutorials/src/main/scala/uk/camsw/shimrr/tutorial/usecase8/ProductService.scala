package uk.camsw.shimrr.tutorial.usecase8

import shapeless.HNil
import uk.camsw.shimrr.Pipeline


trait ProductService {

  def allProducts(): Iterable[BicycleV5]

}

object ProductService {

  def apply(productRepository: ProductRepository, discountService: DiscountService): ProductService = new ProductService {
    val repository: ProductRepository = productRepository

    /**
      * The solution you come up with is horrible
      * Having spent many years in a dungeon, coding endless java mapping
      * layers you wonder whether the library should support some kind of
      * inheritance.  If it did you could declare your discount rule at the
      * version level, then your price rule for versions V1 to V5.
      *
      * It still feels rubbish though
      *  - You have to add a new, identical price rule for every new version
      *  - Inheritance makes you think of overriding and a rule structure that
      * becomes impossible to understand over time
      *  - It doesn't feel representative of what you are trying to express
      *
      * You feel there are two possible requirements here trying to get out
      *  - Perhaps rules could be composed, allowing you to reuse rules at the
      * coproduct Bicycle level
      *  - Perhaps rules should not define a migration from say, V1 ~> V5, but
      * should be more of the form V1 ~> V2 ~> V3 ~> V4 ~> V5
      *
      * You leave this as-is, but return to the shimrr tutorial in the hope it
      * provides some sensible constructs for dealing with these issues
      */
    override def allProducts(): Iterable[BicycleV5] = {
      import shapeless.syntax.singleton.mkSingletonOps
      import uk.camsw.shimrr.context.scoped._
      import uk.camsw.shimrr.syntax._


      implicit val v1 = MigrationContext[BicycleV1](
        'leadTime ->> 7
        :: HNil
      )

      implicit val v2 = MigrationContext[BicycleV2]()

      implicit val v3 = MigrationContext[BicycleV3](
        'discountPercentage ->> ((b: BicycleV3) => discountService.discountFor(b.make, b.model))
          :: HNil
      )

      implicit val v4 = MigrationContext[BicycleV4](
        'price ->> ((b: BicycleV4) => BigDecimal(b.price.toString))
          :: HNil
      )

      implicit val v5 = MigrationContext[BicycleV5]()

      implicit val (p1, p2, p3) = Pipeline[BicycleV1, BicycleV2, BicycleV3]
        .to[BicycleV4]
        .to[BicycleV5]
        .build

      repository.findAll().migrateTo[BicycleV5]
    }
  }
}