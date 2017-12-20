package uk.camsw.shimrr

import org.scalatest.FreeSpec
import uk.camsw.shimmr.Macros.evalImpl

import scala.language.experimental.macros
import scala.reflect.runtime.{universe => ru}


class GenerativeTest extends FreeSpec {

  def eval(expr: String): Unit = macro evalImpl

  "blah" in {

    val tpe = ru.typeOf[Version]
    val clazz = tpe.typeSymbol.asClass
    clazz.knownDirectSubclasses.foreach(println)
    eval("hello")

    // if you want to ensure the type is a sealed trait, println(s"x: $x")
  } // then you can use clazz.isSealed and clazz.isTrait

}




