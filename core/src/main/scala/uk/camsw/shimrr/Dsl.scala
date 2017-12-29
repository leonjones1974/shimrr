package uk.camsw.shimrr

import shapeless.{HList, HNil, LabelledGeneric}

trait MacroDsl {

  def fieldDefaults: HList = HNil


}

class DslInstance[A](b: => Unit) {

}

object DslInstance {
  def create[A]: Dsl[A] = ???
  def apply[A](b: => Unit) = new DslInstance[A](b)
}

trait Dsl[A] extends MacroDsl {
  type FROM = A

  def f[A, ARepr](a:A)(implicit genA: LabelledGeneric.Aux[A, ARepr]) : ARepr = genA to a
}
