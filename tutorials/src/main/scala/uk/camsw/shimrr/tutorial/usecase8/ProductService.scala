package uk.camsw.shimrr.tutorial.usecase8

import shapeless.HNil


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

      val discountRule = MigrationContext[Bicycle](
        'discountPercentage ->> ((b: Bicycle) => discountService.discountFor(b.make, b.model))
        :: HNil
      )

      val price1 = (b: BicycleV1) => BigDecimal(b.price.toString)
      val price2 = (b: BicycleV2) => BigDecimal(b.price.toString)
      val price3 = (b: BicycleV3) => BigDecimal(b.price.toString)
      val price4 = (b: BicycleV4) => BigDecimal(b.price.toString)
      val price5 = (b: BicycleV5) => BigDecimal(b.price.toString)

      implicit val v1 = MigrationContext[BicycleV1](
          'price ->> price1
          :: HNil
      ) ++ discountRule

      implicit val v2 = MigrationContext[BicycleV2](
          'price ->> price2
          :: HNil
      ) ++ discountRule

      implicit val v3 = MigrationContext[BicycleV3](
          'price ->> price3
          :: HNil
      ) ++ discountRule

      implicit val v4 = MigrationContext[BicycleV4](
          'price ->> price4
          :: HNil
      ) ++ discountRule

      implicit val v5 = MigrationContext[BicycleV5](
          'price ->> price5
          :: HNil
      ) ++ discountRule

//      repository.findAll().migrateTo[BicycleV5]
      ???
    }
  }
}