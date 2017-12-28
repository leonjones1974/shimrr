package uk.camsw.shimrr.macros

import scala.language.experimental.macros
import scala.meta.Defn
import scala.reflect.macros.blackbox
import scala.reflect.macros.blackbox

//@compileTimeOnly("enable macro paradise to expand macro annotations")
//class migration extends StaticAnnotation {
//  def macroTransform(annottees: Any*) = macro DefaultMacro.impl
//}
//

import scala.meta._

class main extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val q"object $name { ..$stats }" = defn
    val main = q"def main(args: Array[String]): Unit = { ..$stats }"
    q"object $name { $main }"
  }
}

class sayHello extends scala.annotation.StaticAnnotation {

  import scala.reflect.runtime._


  inline def apply(defn: Any): Any = meta {
    val q"trait $name[..$tparams] extends ..$bases { ..$body }" = defn
    println("I AM HEre")

    //       class Fish[$tpe] {
    //         def doSomething[$tpe](v: $tpe): Any = {
    //          "hello"
    //         }
    //       }

//    q"val q: $tpe" = MacroUtil.checkType[String]()
    //
//    val xx = q"_root_.uk.camsw.shimrr.macros.ReflectionMacros.checkType[$t]()"
    //        implicit def strRules: DerivedRules[String] = new DerivedRules[String] {}
    val varName = Pat.Var.Term(Term.Name(name.value))
    q"""
      println("tired")
      val amp = 10

//      @exports
      object ${Term.Name(name.value)} {
        def getMe = "Hello me for " + this.getClass.getName

        val $varName: String = "Hello"

      }


     """
  }
}


class Hello extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case cls@Defn.Class(_, _, _, ctor, template) =>
        val hello = q"""def hello: Unit = println("Hello")"""
        val stats = hello +: template.stats.getOrElse(Nil)
        cls.copy(templ = template.copy(stats = Some(stats)))
    }
  }
}


object DefaultMacro {


//  def impl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
//    import c.universe._
////    val inputs: List[Tree] = annottees.map(_.tree).toList
////    val tree = inputs(0)
////    val q"val $list:List[$t]= $files" = tree
////    print(show(q"""implicit val fl1:$t = $files(0)"""))
//
//    //            implicit val ctx = uk.camsw.shimrr.context.scoped.MigrationContext[uk.camsw.shimrr.MyFish](shapeless.HNil)
//    //            implicit val ctx2 = uk.camsw.shimrr.context.scoped.MigrationContext[uk.camsw.shimrr.MyOtherFish](shapeless.HNil)
//
//    val rule: Tree = annottees.head.tree
//    println(s"the rule: ${rule}")
//    c.typecheck(q"(??? : ${rule.duplicate})").tpe
//
////    val q"val $scope:uk.camsw.shimrr.MigrationRules= $s" = rule
//    println("6")
//
//
//    c.Expr[Any] {
//      q"""
//          println("well you invoked the macro at least - well done")
//
//          println("hello")
//
//
//      """
//    }
//      //    println("materialized scope: " + $s)
//  }
}

