package uk.camsw.shimrr.tutorial.usecase9

sealed trait Bicycle {
  def make: String
  def model: String
}

case class BicycleV1(make: String, model: String, price: Float) extends Bicycle

case class BicycleV2(make: String, model: String, price: Float, leadTime: Int) extends Bicycle

case class BicycleV3(make: String, model: String, price: Float) extends Bicycle

case class BicycleV4(make: String, model: String, price: Float, discountPercentage: Int) extends Bicycle

case class BicycleV5(make: String, model: String, price: BigDecimal, discountPercentage: Int) extends Bicycle

case class BicycleV6(make: String, model: String, retailPrice: BigDecimal, discountPercentage: Int) extends Bicycle

