package uk.camsw.shimrr.macros

import scala.language.experimental.macros
import scala.meta._
import scala.meta.dialects.Scala212



class migration extends scala.annotation.StaticAnnotation {


  inline def apply(defn: Any): Any = meta {


    val x=
      """
         case class MyClass(stringField1: String = "str1")

      """.parse[Source].get
    println(s"stats: ${x.stats}")
    case class Builder() {
      def addField(x: Stat) = {
        println(s"adding field: ${x}")
      }


      def addField[V](t: Tuple2[Symbol, V]): Unit = {
        println(s"**** adding: $t")
      }

    }

    val b = Builder()

    defn match {
      case q"object $name extends ..$supers { ..$stats }" =>
//        if (stats.isEmpty) throw new RuntimeException(
//          s"""First line of migration rules must specify a 'From' type of the form
//             |   type FROM = FooV1
//             | No such definition found in ${defn.toString()}
//             |   """.stripMargin)
//
//        val from = stats.head
//        println(s"From is: ${from}")
        val body = stats


        val fieldDefaults = stats.collect {
//          case x@q"(..$exprs)" =>
//            println(s"I think i got a tuple of :$exprs")
//            x
          case x@q"$l $op[..$tpes] $r" =>
            println("WOOT I got an infix - did i?")
            println(s"l: ${l}")
            println(s"n: $op")
            println(s"r: ${r}")
            println(s"tokens: ${l.tokens}")
            l.tokens.toSeq.foreach(t => {
              println(s"token: ${t}")
            }
            )

            val paramName = l.tokens.toList(5).text.drop(1).dropRight(1)
            println(s"My name is: ${paramName}")
            println(s"value is: ${r.tokens}")
            println(s"token size: ${r.tokens.length}")
            val paramValue = r.tokens.toList.mkString("")
//            val paramType = Helper.findType(paramValue)
            println(s"param value is: ${paramValue}")
//            println(s"param type is: ${paramType}")
            b.addField(x)
            s"""
               addField($x)

               """.parse[Stat].get
//          case x =>
//            println(s"** Unrecognised **: ${x}")
//            x

        }


        println(s"body is: ${body}")
        val objName = Term.Name(name.value)
        val ctxName = Term.Name(name.value + "_rules")

        val zz = q"""("leon" -> "jones")"""
        println(s"The field default are: ${fieldDefaults}")
        println(s"Generating rules context: ${ctxName}")
        q"""
         object $objName extends ..$supers {
           ..$body;
           object exports {
             ..${x.stats};
             val zz = MyClass("str2")

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
