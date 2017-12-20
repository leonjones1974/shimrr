package uk.camsw.shimrr

import shapeless.{Coproduct, Generic}

object TestUtil {

  def toCoproduct[A, ARepr <: Coproduct](a: A)(
                                        implicit
                                        genA: Generic.Aux[A, ARepr]): ARepr = genA to a

}
