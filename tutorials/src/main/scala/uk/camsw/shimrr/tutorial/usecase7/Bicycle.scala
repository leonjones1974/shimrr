package uk.camsw.shimrr.tutorial.usecase7

/**
  * You abandon the idea of pulling up price, it will be the wrong type in the
  * end anyway!
  */
sealed trait Bicycle{
  def make: String
  def model: String
}

case class BicycleV1(make: String, model: String, price: Float) extends Bicycle

case class BicycleV2(make: String, model: String, price: Float, leadTime: Int) extends Bicycle

case class BicycleV3(make: String, model: String, price: Float) extends Bicycle

case class BicycleV4(make: String, model: String, price: Float, discountPercentage: Int) extends Bicycle

/**
  * You press on and create V5 with a different type for price
  */
case class BicycleV5(make: String, model: String, price: BigDecimal, discountPercentage: Int) extends Bicycle


