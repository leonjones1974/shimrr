package uk.camsw.shimrr.tutorial.usecase4

sealed trait Bicycle

case class BicycleV1(make: String, model: String, price: Float) extends Bicycle

case class BicycleV2(make: String, model: String, price: Float, leadTime: Int) extends Bicycle

case class BicycleV3(make: String, model: String, price: Float) extends Bicycle

/**
  * You add the new version, hoping that discounts will always be integer values in the future
  */
case class BicycleV4(make: String, model: String, price: Float, discountPercentage: Int) extends Bicycle


