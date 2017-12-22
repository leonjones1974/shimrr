package uk.camsw.shimmr

import scala.language.dynamics
import scala.language.experimental.macros

import scala.reflect.macros.whitebox

object migrationSupported extends Dynamic {

  def apply[T](implicit t: T): T {} = t

  def selectDynamic(tpeTree: String): Any = macro implicitlyImpl

  def implicitlyImpl(c: whitebox.Context)(tpeTree: c.Tree): c.Tree = {
    import c.universe._
    import internal._, decorators._

    val q"${tpeString: String}" = tpeTree
    println(s"tpe: ${tpeString}")
    val parsed = c.parse("null : "+tpeString)
    println(s"parsed: ${parsed}")
    val tpe = c.typecheck(parsed, mode = c.TYPEmode).tpe
    println(s"type: ${tpe}")
    val inferred = c.inferImplicitValue(tpe)
//    if (inferred.isEmpty) throw new RuntimeException("nope")

//    println(s"inferred is: ${inferred}")

//    Literal(Constant(reify{inferred}))
    inferred
  }
}

// Exiting paste mode, now interpreting.
