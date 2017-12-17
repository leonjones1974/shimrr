package uk.camsw.shimrr

import macrocompat.bundle
import scala.language.experimental.macros
import scala.reflect.macros.{blackbox, whitebox}

object Test {
  def foo: Int = macro TestMacro.fooImpl

  def bar(i: Int): String = macro TestMacro.barImpl

  def baz(is: Int*): Int = macro TestMacro.bazImpl

  def helloWorld(format: Symbol, name: String): String = macro TestMacro.helloWorldImpl
//  def helloWorld2(format: Symbol): String = macro TestMacro.helloWorldImpl2

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

//  def helloWorldImpl2(format: c.Expr[scala.Symbol]): Tree = {
//    import shapeless.syntax.singleton._
//    format match {
////      case This(_) =>
////        true
////      case l@Literal(_) =>
////        println(s"got a literal: ${l}")
////        true
//      case ex:Expr[scala.Symbol] =>
////        println(s"Got an exp: ${zz}")
//        //println(s"evaluated: ${c.eval(ex)}")
//        val x = c.Expr[scala.Symbol](c.untypecheck(format.tree.duplicate))
//        println(s"compile-time value is: ${c.eval(x)}")
//        q""
//      case i@Ident(name) =>
//        println(s"got an ident: ${name}")
//        println(s"ident: ${i}")
//        println(s"1 ${i.isBackquoted}")
//        println(s"2${i.isDef}")
//        println(s"3 ${i.isEmpty}")
//        println(s"4 ${i.isTerm}")
//        println(s"5 ${i.isType}")
//        println(s"6 ${i.symbol}")
//
//        val arg = c.parse(s"${name}")
//        println(s"Arg is: ${arg}")
//        println("resolved arg is: " + q"${name.toTermName}")
//        println(s"evaluated: ${c.eval(format)}")
////        println(showRaw(c.enclosingClass))
//        println(showRaw(c.enclosingMethod))
//
////        c.enclosingClass.children.find{
////          case x:ValDef =>
////            println(s"got valDef: ${x}")
////            true
////          case o =>
////            println()
////            println(s"Got: ${o}")
////            true
////        }
////        val Constant(Literal(v: Symbol)) = arg
////        println(s"v: ${v}")
//
//
//        val f: c.Tree = c.parse(s"sayItSym")
//
//        val z = q"$f($i)"
//        println(s"full expr is: ${z}")
//        z
////      case select@Select(objExpr, term) =>
////        println(s"got something else: ${select}")
////        println(s"term: ${term}")
////        println(s"obj: ${objExpr}")
////        val x = q"$objExpr.${term.toTermName}"
////        println(s"expression is: ${x}")
////        val f: c.Tree = c.parse(s"say_fish")
////        val z = q"$f($x)"
////        println(s"full expr is: ${z}")
////        x
////
////
////      //        isConstant(c) (objExpr) && select.symbol.isStable
////      //for implicit values
////      //      case Apply(TypeApply(Select(Select(This(TypeName("scala")), TermName("Predef")), TermName("implicitly")), _), _) =>
////      //        true
////      case otherwise =>
////        println(s"otherwise: ${otherwise}")
////        println(s"reified: ${reify(otherwise)}")
////        false
//    }
////    //    val Literal(Constant(s_format: String)) = format.tree
////    //    val f: c.Tree = c.parse(s"say_$s_format()")
////    val f: c.Tree = c.parse(s"""sayIt("leon")""")
////    q"$f"
//  }

  def helloWorldImpl(format: c.Tree, name: c.Expr[String]): Tree = {

    val Literal(Constant(s_name: String)) = name.tree

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
      case i@Ident(name) =>
        println(s"got an ident: ${name}")
        println(s"ident: ${i}")
        val f: c.Tree = c.parse(s"sayIt_$name")
//        val z = q"$f($x)"
        println(s"full expr is: ${f}")
        f
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