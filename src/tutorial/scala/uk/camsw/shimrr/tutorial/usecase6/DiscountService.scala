package uk.camsw.shimrr.tutorial.usecase6

/**
  * The service has been updated to provide make/ model specific discounts
  */
case class DiscountService(defaultDiscountPercentage: Int ) {
  val defaultProductDiscount: () => Int = () => defaultDiscountPercentage

  def discountFor(model: String, make: String): Int = {
    (model, make) match {
      case ("Qu-Ax", "Penny Farthing Gentlemen's bike") => 50
      case otherise => defaultProductDiscount()
    }
  }
}
