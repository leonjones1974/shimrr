package uk.camsw.shimrr.macros

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
        println(s"body is: ${body}")
        val objName = Term.Name(name.value)
        val ctxName = Term.Name(name.value + "_rules")

        //
        println(s"Generating rules context: ${ctxName.value}")
        q"""
         object $objName extends _root_.uk.camsw.shimrr.Dsl {
           $from
           ..$body;
           object exports {
             implicit def $ctxName: _root_.uk.camsw.shimrr.Rules[FROM] = new _root_.uk.camsw.shimrr.Rules[FROM] {}
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

}
