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

      /*
        it feels like we might need hierarchical rules (??)
        i don't really want to have to pull everything down as soon as the model diverges
        */
      implicit val v1 = MigrationContext[BicycleV1](
        'discountPercentage ->> discount1
          :: 'price ->> BigDecimal("1.2")
          :: HNil
      )

      implicit val v2 = MigrationContext[BicycleV2](
        'discountPercentage ->> discount2
          :: 'price ->> BigDecimal("1.2")
          :: HNil
      )

      implicit val v3 = MigrationContext[BicycleV3](
        'discountPercentage ->> discount3
          :: 'price ->> BigDecimal("1.2")
          :: HNil
      )

      implicit val v4 = MigrationContext[BicycleV4](
        'discountPercentage ->> discount4
          :: 'price ->> BigDecimal("1.2")
          :: HNil
      )

      implicit val v5 = MigrationContext[BicycleV5](
        'discountPercentage ->> discount5
          :: 'price ->> BigDecimal("1.2")
          :: HNil
      )

      repository.findAll().migrateTo[BicycleV5]
    }
  }
}