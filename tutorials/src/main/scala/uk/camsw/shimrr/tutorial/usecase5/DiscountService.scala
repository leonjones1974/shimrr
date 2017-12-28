package uk.camsw.shimrr.tutorial.usecase5

/**
  * This is the stub you've been given for the default product discount
  */
case class DiscountService(defaultDiscountPercentage: Int ) {
  val defaultProductDiscount: () => Int = () => defaultDiscountPercentage
}
