package uk.camsw.shimrr.tutorial.usecase4

import shapeless.HNil
import uk.camsw.shimrr.context.MigrationContext

trait ProductService {

  /**
    * Again, you update the service to return V4
    */
  def allProducts(): Iterable[BicycleV4]

}

object ProductService {

  def apply(productRepository: ProductRepository): ProductService = new ProductService {
    val repository: ProductRepository = productRepository

    /**
      * The shimrr documentation refers to a Global Migration Context that can be used to
      * define migration rules for an entire coproduct (in this case Bicycle).
      * Again, for this use case there is no need to migrate via intermediate versions.
      *
      * Everything can be migrated directly to V4 providing we have a rule that says:
      *  - Whenever the field 'discountPercentage' is missing, add it with a default value of 10
      *
      * Sounds easy enough, so you change the signature and try to migrate to V4
      * You hit the compile keystroke ...
      * ...
      * ...
      * ...
      * After a long pause your compiler fails with the scary error:
      * Error:(35, 37) could not find implicit value for parameter
      * m: uk.camsw.shimrr.Migration[uk.camsw.shimrr.examples.step4.Bicycle,uk.camsw.shimrr.examples.step4.BicycleV4]
      *     repository.findAll().migrateTo[BicycleV4]
      *
      * TIP: Tracking down missing implicits can be difficult although changes like the one being made are quite local to the change so it should be
      * pretty obvious we're missing rules for our new discountPercentage field.  For more detailed information you could try the
      * Splain compiler plugin:
      * https://github.com/tek/splain
      * Follow the instructions in the splain README, noting that the compiler plugin must go in build.sbt and NOT in plugins.sbt as you might expect
      *
      * Thinking about it you realise that, unlike field dropping, you aren't going to get field defaulting rules for free
      * The good news it that your project wont compile until you fix it!
      *
      */
    override def allProducts(): Iterable[BicycleV4] = {
      import uk.camsw.shimrr.context.global._
      import uk.camsw.shimrr.syntax._

      // Another new import, this time a shapeless one.  This import gives you access to the ->> symbol, which allows you to provide
      // type-safe singleton keys to represent your field rules
      import shapeless.syntax.singleton.mkSingletonOps


      //noinspection TypeAnnotation
      // Because you need to specifiy a rule you need to create a migration context.  In this case it will be a global one because
      // you imported the global context
      // For fun, have a look at the type of allToV4 and you'll see why we've used the noinspection annotation
      implicit val allToV4 = MigrationContext(
        defaults = 'discountPercentage ->> 10 :: HNil
      )

      // You rebuild and everything is happy
      // You run the test and everything is happy
      // Because you are curious you change the field name in the rule and it breaks compilation again
      repository.findAll().migrateTo[BicycleV4]

    }
  }
}
