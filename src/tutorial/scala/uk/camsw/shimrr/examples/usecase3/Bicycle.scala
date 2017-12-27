package uk.camsw.shimrr.examples.usecase3

sealed trait Bicycle
case class BicycleV1(make: String, model: String, price: Float) extends Bicycle
case class BicycleV2(make: String, model: String, price: Float, leadTime: Int) extends Bicycle

/**
  * You introduce V3.  While you recognise that it's identical to V1 you
  * remember that you don't have the ability to re-write to the store
  * You are already worried about the migration complexity and feel that having versions
  * going backwards is just going to confuse everybody!
  */
case class BicycleV3(make: String, model: String, price: Float) extends Bicycle


