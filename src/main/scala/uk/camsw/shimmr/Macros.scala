package uk.camsw.shimmr

import scala.reflect.macros.blackbox

object Macros {

  def evalImpl(c: blackbox.Context)(expr: c.Expr[String]): c.Expr[Unit] = {
    import c.universe._
    println(c.eval(expr))
    reify{()}
  }
}