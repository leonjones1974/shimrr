package uk.camsw.shimrr.macros
import scala.language.experimental.macros
import scala.reflect.macros.{blackbox, whitebox}

object MacroUtil {

  def checkType[A](): Any = macro ReflectionMacros.checkType[A]

}


object ReflectionMacros {
  def checkType[A: c.WeakTypeTag](c: whitebox.Context)(): c.Expr[Any] = {
    import c.universe._



    c.Expr(
      q"""
         println("hello from sub")
         val imp = 10
       """
    )
  }
}
