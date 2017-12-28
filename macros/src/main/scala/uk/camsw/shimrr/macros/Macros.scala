package uk.camsw.shimrr.macros

import scala.language.experimental.macros
import scala.meta._

class migration extends scala.annotation.StaticAnnotation {

  inline def apply(defn: Any): Any = meta {
    val q"object $name { ..$stats }" = defn
    if (stats.isEmpty) throw new RuntimeException(
      s"""First line of migration rules must specify a 'From' type of the form
        |   type FROM = FooV1
        | No such definition found in ${defn.toString()}
        |   """.stripMargin)

    val from = stats.head
    val objName = Term.Name(name.value)
    val ctxName = Term.Name(name.value + "_rules")

    println(s"Generating rules context: ${ctxName.value}")
    q"""
       object $objName {
         $from
         println("created the rules object - creating the implicit")
         object exports {
           implicit def $ctxName: _root_.uk.camsw.shimrr.Rules[FROM] = new _root_.uk.camsw.shimrr.Rules[FROM] {}
         }
       }
     """
  }
}
