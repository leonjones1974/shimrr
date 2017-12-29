package uk.camsw.shimrr.macros

import shapeless.{HList, HNil}

import scala.language.experimental.macros
import scala.meta._

class migration extends scala.annotation.StaticAnnotation {


  inline def apply(defn: Any): Any = meta {
    defn match {
      case q"object $name extends ..$supers { ..$stats }" =>
        if (stats.isEmpty) throw new RuntimeException(
          s"""First line of migration rules must specify a 'From' type of the form
             |   type FROM = FooV1
             | No such definition found in ${defn.toString()}
             |   """.stripMargin)

        val from = stats.head
        val body = stats.tail
        body.foreach(println(_))

        val fieldDefaultsF = body.foldLeft[HList](HNil)((acc, stat) => stat match {
          case x@q"$l $op[..$tpes] $r" =>
            println("WOOT I got an infix - did i?")
            println(s"l: ${l}")
            println(s"n: $op")
            println(s"r: ${r}")
            val q"""$rule""" = stat
            rule :: acc
          case _ => acc
        })

        val fieldDefaults = body.collect {
          case x@q"$l $op[..$tpes] $r" =>
            println("WOOT I got an infix - did i?")
            println(s"l: ${l}")
            println(s"n: $op")
            println(s"r: ${r}")
            x
        }

        println(s"body is: ${body}")
        val objName = Term.Name(name.value)
        val ctxName = Term.Name(name.value + "_rules")


        println(s"The field default are: ${fieldDefaults}")
        println(s"Generating rules context: ${ctxName}")
        q"""
         object $objName extends ..$supers {
           $from
           ..$body;
           object exports {
             implicit def $ctxName = _root_.uk.camsw.shimrr.context.scoped.MigrationContext[FROM](fieldDefaults)
           }
       }
     """
      case _ => throw new RuntimeException(
        """Migration macro can only be applied to
          |     'object <name>
          | or
          |     "object name extends <supers>
          | No such definition found in ${defn.toString()}
          |     """.stripMargin)
    }

  }
  //

}
