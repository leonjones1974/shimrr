package uk.camsw.shimrr.context

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.reflect.macros.{blackbox, whitebox}
import scala.language.experimental.macros

@compileTimeOnly("enable macro paradise to expand macro annotations")
class myMacro extends StaticAnnotation {
  def macroTransform(annottees: Any*) = macro DefaultMacro.impl
}

object DefaultMacro {
  def impl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
//    val inputs: List[Tree] = annottees.map(_.tree).toList
//    val tree = inputs(0)
//    val q"val $list:List[$t]= $files" = tree
//    print(show(q"""implicit val fl1:$t = $files(0)"""))
    c.Expr[Any] {
      q"""
            implicit val ctx = uk.camsw.shimrr.context.scoped.MigrationContext[uk.camsw.shimrr.MyFish](shapeless.HNil)
            implicit val ctx2 = uk.camsw.shimrr.context.scoped.MigrationContext[uk.camsw.shimrr.MyOtherFish](shapeless.HNil)
      """
    }
  }
}