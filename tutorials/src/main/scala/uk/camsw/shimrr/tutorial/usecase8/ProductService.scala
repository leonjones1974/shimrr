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
      * The solution you come up with using the constructs you know of thus far
      * ... is horrible
      *
      * Having spent many years in a dungeon, coding endless java mapping
      * layers you wonder whether the library should support some kind of
      * inheritance.  If it did you could declare your discount rule
      * and field type transformation at the version level
      *
      * It still feels rubbish though
      *  - You have to add a new, identical price rule for every new version
      *    due to the type-safety of the parameterized defaulter
      *  - Inheritance makes you think of overriding and a rule structure that
      *    becomes impossible to understand over time
      *  - It doesn't feel representative of what you are trying to express
      *
      * You feel there are two possible requirements here trying to get out
      *  - Perhaps rules could be composed, allowing you to reuse rules at the
      *    coproduct Bicycle level
      *  - But,.. oerhaps rules should not define a migration from say, V1 ~> V5, but
      *    should be more of the form V1 ~> V2 ~> V3 ~> V4 ~> V5
      *
      * You refer to the documentation again and stumble upon the notion of pipelines!
      */
    override def allProducts(): Iterable[BicycleV5] = {
      import shapeless.syntax.singleton.mkSingletonOps
      import uk.camsw.shimrr.context.scoped._
      import uk.camsw.shimrr.syntax._

      // Now each of our rules only introduce the field defaults required
      // in order to transition to the 'next' version
      implicit val v1 = MigrationContext[BicycleV1](
        'leadTime ->> 7
        :: HNil
      )

      // No rules at all here as it was a field drop.  We still need to declare a context though
      implicit val v2 = MigrationContext[BicycleV2]()

      // Here we apply our discount rule, only when we try to migrate from V3->V4
      implicit val v3 = MigrationContext[BicycleV3](
        'discountPercentage ->> ((b: BicycleV3) => discountService.discountFor(b.make, b.model))
          :: HNil
      )

      // Our price transformation happens only at V4 now
      implicit val v4 = MigrationContext[BicycleV4](
        'price ->> ((b: BicycleV4) => BigDecimal(b.price.toString))
          :: HNil
      )

      // V5 is our current version - but we need to specify an empty context
      implicit val v5 = MigrationContext[BicycleV5]()

      // Finally we build our pipeline (V1 -> V2 -> V3 -> V4 -> V5)
      // There must
      //   - be valid migration rules in scope for each transition
      // The output is multiple pipelines, all of which must be made implicits
      // There will always be n-2 pipelines, of the form:
      //    V1 -> V2 -> V3 -> V4 -> V5
      //    V2 -> V3 -> V4 -> V5
      //    V3 -> V4 -> V5
      // because the other 2 transitions
      //    V4 -> V5
      //    V5 -> V5
      // ... are migrations rather than pipelines and you've already bought those
      // into scope when you declared the rules
      //
      // ANOTHER DISCLAIMER:
      // This is still 'under-the-hood' stuff to help with understanding. In the
      // next tutorial we'll look at using the DSL to express our migration pipeline
      implicit val (p1, p2, p3) = Pipeline[BicycleV1, BicycleV2, BicycleV3]
        .to[BicycleV4]
        .to[BicycleV5]
        .build

      repository.findAll().migrateTo[BicycleV5]

    }
  }
}