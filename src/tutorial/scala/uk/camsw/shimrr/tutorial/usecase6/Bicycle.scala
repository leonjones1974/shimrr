package uk.camsw.shimrr.tutorial.usecase6

sealed trait Bicycle{
  def make: String
  def model: String
}

case class BicycleV1(make: String, model: String, price: Float) extends Bicycle

case class BicycleV2(make: String, model: String, price: Float, leadTime: Int) extends Bicycle

case class BicycleV3(make: String, model: String, price: Float) extends Bicycle

case class BicycleV4(make: String, model: String, price: Float, discountPercentage: Int) extends Bicycle


