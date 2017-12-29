package uk.camsw.shimrr

import shapeless.{HList, HNil}

trait MacroDsl {

  def fieldDefaults: HList = HNil

}

trait Dsl[A] extends MacroDsl {
  type FROM = A

}
