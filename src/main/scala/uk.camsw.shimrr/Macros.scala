package uk.camsw.shimrr

import macrocompat.bundle
import scala.language.experimental.macros
import scala.reflect.macros.{blackbox, whitebox}

object Test {
  def foo: Int = macro TestMacro.fooImpl

  def bar(i: Int): String = macro TestMacro.barImpl

  def baz(is: Int*): Int = macro TestMacro.bazImpl

  def helloWorld(format: String): String = macro TestMacro.helloWorldImpl

  def sayIt(): String = {
    "I said it"
  }
}

@bundle // macro-compat addition
class TestMacro(val c: whitebox.Context) {

  import c.universe._

  def fooImpl: Tree = q""" 23 """ // explicit return type : Tree required

  def barImpl(i: Tree): Tree = q""" "bar" """

  def bazImpl(is: Tree*): Tree = q""" 13 """

//  def helloWorldImpl(format: c.Tree): Tree = {
//    format match {
//      case This(_) =>
//        true
//      case l@Literal(_) =>
//        println(s"got a literal: ${l}")
//        true
//      case Ident(name) =>
//        println(s"got an ident: ${name}")
//      //        ident.symbol.isStable
//      case select@Select(objExpr, term) =>
//        println(s"got something else: ${select}")
//        println(s"term: ${term}")
//        println(s"obj: ${objExpr}")
//        val x = q"$objExpr.${term.toTermName}"
//        println(s"expression is: ${x}")
//        val f: c.Tree = c.parse(s"say_fish")
//        val z = q"$f($x)"
//        println(s"full expr is: ${z}")
//        x
//
//
//      //        isConstant(c) (objExpr) && select.symbol.isStable
//      //for implicit values
//      //      case Apply(TypeApply(Select(Select(This(TypeName("scala")), TermName("Predef")), TermName("implicitly")), _), _) =>
//      //        true
//      case otherwise =>
//        println(s"otherwise: ${otherwise}")
//        println(s"reified: ${reify(otherwise)}")
//        false
//    }
//    //    val Literal(Constant(s_format: String)) = format.tree
//    //    val f: c.Tree = c.parse(s"say_$s_format()")
//    val f: c.Tree = c.parse(s"say_fish")
//    q"$f"
//  }

  def helloWorldImpl(format: c.Tree): Tree = {
    format match {
      case select@Select(objExpr, term) =>
        println(s"got something else: ${select}")
        println(s"term: ${term}")
        println(s"obj: ${objExpr}")
        val x = q"$objExpr.${term.toTermName}"
        println(s"expression is: ${x}")
        val f: c.Tree = c.parse(s"sayIt")
        val z = q"$f($x)"
        println(s"full expr is: ${z}")
        z
    }
  }
}

//object Macros {
//

//
//}

//  import cats.instances.string._
//


//  def helloWorldSymbol(sym: Symbol) = macro helloWorldSymbolImpl
//
//
//  def helloWorldSymbolImpl(c: blackbox.Context)(sym: c.Expr[Symbol]): c.Expr[Unit] = {
//    import c.universe._
//    //    val Literal(Constant(s_format: String)) = format.tree
//    //     reify(print(c.Expr[Any](s_format))).tree
//    //    println("A message from the compiler macro")
//    //    c.Expr[Unit](???)
//    //    reify(println(s"Hello World: " + format.splice))
//    import shapeless.syntax.singleton._
//
////    val Literal(Constant(sym_c)) = sym.tree
////    reify(withMacroMonoid['fish](sym.splice.asInstanceOf['fish]))
//  }

//}

//@bundle
//class TestMacro(val c: whitebox.Context) {
//  import c.universe._
////  def helloWorldImpl(c: blackbox.Context)(format: c.Expr[String]): c.Expr[Unit] = {
////    import c.universe._
////    //    val Literal(Constant(s_format: String)) = format.tree
////    //     reify(print(c.Expr[Any](s_format))).tree
////    //    println("A message from the compiler macro")
////    //    c.Expr[Unit](???)
////    reify(println(s"Hello World: " + format.splice))
////
////  }
//

//}