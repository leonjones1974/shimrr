package uk.camsw.shimrr.tutorial.usecase7

import shapeless.HNil


trait ProductService {

  def allProducts(): Iterable[BicycleV5]

}

object ProductService {

  def apply(productRepository: ProductRepository, discountService: DiscountService): ProductService = new ProductService {
    val repository: ProductRepository = productRepository

    override def allProducts(): Iterable[BicycleV5] = {
      import shapeless.syntax.singleton.mkSingletonOps
      import uk.camsw.shimrr.syntax._
      import uk.camsw.shimrr.context.scoped._

      // We still need our rule for defaulting the discounts
      val discount1 = (b: BicycleV1) => discountService.discountFor(b.make, b.model)
      val discount2 = (b: BicycleV2) => discountService.discountFor(b.make, b.model)
      val discount3 = (b: BicycleV3) => discountService.discountFor(b.make, b.model)
      val discount4 = (b: BicycleV4) => discountService.discountFor(b.make, b.model)
      val discount5 = (b: BicycleV5) => discountService.discountFor(b.make, b.model)

      val price1 = (b: BicycleV1) => BigDecimal(b.price.toString)
      val price2 = (b: BicycleV2) => BigDecimal(b.price.toString)
      val price3 = (b: BicycleV3) => BigDecimal(b.price.toString)
      val price4 = (b: BicycleV4) => BigDecimal(b.price.toString)
      val price5= (b: BicycleV5) => BigDecimal(b.price.toString)

      /*
        it feels like we might need hierarchical rules (??)
        i don't really want to have to pull everything down as soon as the model diverges
        */
      implicit val v1 = MigrationContext[BicycleV1](
        'discountPercentage ->> discount1
          :: 'price ->> price1
          :: HNil
      )

      implicit val v2 = MigrationContext[BicycleV2](
        'discountPercentage ->> discount2
          :: 'price ->> price2
          :: HNil
      )

      implicit val v3 = MigrationContext[BicycleV3](
        'discountPercentage ->> discount3
          :: 'price ->> price3
          :: HNil
      )

      implicit val v4 = MigrationContext[BicycleV4](
        'discountPercentage ->> discount4
          :: 'price ->> price4
          :: HNil
      )

      implicit val v5 = MigrationContext[BicycleV5](
        'discountPercentage ->> discount5
          :: 'price ->> price5
          :: HNil
      )

      repository.findAll().migrateTo[BicycleV5]

    }
  }
}