package uk.camsw.shimrr

import uk.camsw.shimrr.test.MigrationFreeSpecMacros

import scala.reflect.macros.blackbox

object DslOps {

  def evalWithinContext[T](block: => T): T = macro DslMacros.evalWithinContext[T]

}

object DslMacros {

  def evalWithinContext[T: c.WeakTypeTag](c: blackbox.Context)(block: c.Tree): c.Expr[T] = {
    import c.universe._
    c.Expr(
      q"""

       println("I am within a block")
       $block
       println("Having another go before returning")
       $block
     """
    )
  }
}
