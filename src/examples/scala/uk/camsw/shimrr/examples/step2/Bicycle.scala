package uk.camsw.shimrr.examples.step2

/**
  * You decide to rename Bicycle to BicycleV1.  Fortunately you already have
  * a deserialization mechanism in place, so you don't need to worry about it
  * as part of this particular story!
  *
  * Having looked at the repository interface you realise you need some kind of
  * abstraction at that level.  You introduce a trait which you make sealed at this
  * stage, somehow predicting that will be important later on in this tutorial
  */
sealed trait Bicycle
case class BicycleV1(make: String, model: String, price: Float) extends Bicycle
case class BicycleV2(make: String, model: String, price: Float, leadTime: Int) extends Bicycle

