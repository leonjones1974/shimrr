package uk.camsw.shimrr.macros


import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.{blackbox, whitebox}
import macrocompat.bundle

trait FromTuple[Z, T] {
  def fromTuple(t: T): Z
}

object Macro {
  def dsl[A](b: => Unit): Any = macro MacroImpl.dsl[A]
}

@compileTimeOnly("enable macro paradise to expand macro annotations")
class migrationF[A] extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro MacroBundle.dslF
}

@bundle
class MacroBundle(val c: whitebox.Context) {
  import c.universe._
  def dslF(annottees: Tree*): Tree = {
    import c.universe._


    println(showRaw(annottees))
    annottees match {

      case x@List(q"""val $termName = $ass""") =>
        x.collect{
          case q"""val $tName = $expr""" =>
            println("woot if I find this I think i have all the pieces")
            println(s"expr: ${expr}")
            expr match {
              case 	q"new { ..$stat } with ..$inits { $self => ..$stats }" =>
                println("found an anon one have i?")
                println(s"${stat}")
                println(s"init: ${inits}")
                inits.head match {
                  case tq"$dsl[$typ]" =>
                    println("WOOOOOOOT I have found the dsl type")
                    println(s"Which is: ${dsl}")
                    println(s"Therefore I can infer my from type to be: ${typ}")
                    q"()"
                  case zz =>
                    println(s"Unmatched inits: ${zz}")
                }
                q"()"
              case q"$mods class anon extends ..$supers { ..$stats }" =>
                println("WOoort, I have found: " )
                q"()"
              case q"new $init" =>
                println(s"Woot, got an init: ${init}")
                q"()"
              case e =>
                println(s"Unrecognised exp:${e}")
                q"()"
            }
            q"()"
          case z =>

            println(s"Unable to locate what i want to locate ${z}")
            q"()"
        }

        println(s"X: ${x}")
        println("FOUND THE RULES VAL")
        println(s"term name is: ${termName}")
        q"""
           object $termName {
             val x = "fish"
           }
        """

      case x@List(q"$mods class $termName[$rType] extends ..$parents { $self => ..$body }") =>

        println(showRaw(c.macroApplication))
        val cc = q"""
           object Str1Str2Rules {
             object exports {
               val x = "fish"
             }
           }
         """
        println(s"I would create: ${cc}")
        cc
      case x =>
        println(s"Unrecognised: ${x}")
        c.abort(c.enclosingPosition,
          s"""@migration can only be applied to definition
            |   class SomeClass[FROM_TYPE] { .. }
            |But was: [${x}]
          """.stripMargin)
        q"()"
//        val Exports(List(), PriorityArg(priority)) = c.prefix.tree
    }
  }
}

object MacroImpl {

  def dsl[A: c.WeakTypeTag](c: blackbox.Context)(b: c.Expr[Any]): c.Expr[Any] = {
    import c.universe._

    val pq"($a, $d)" = pq"(Some(1), Some(2))"
    println(s"got: ${a} and ${d}")

    b.tree.children match {

      case x: List[_] =>
        println(s"I got a list: ${x}")
        val cls = x.headOption.map(y => {
          println(s"Got a y: ${y}")
          y match {
            case Apply(t, List(sym, v)) =>
              println(s"found an apply: yy: ${sym} -> $v")
              val tsym = c.typecheck(sym)
              println(s"type of sym is: ${tsym.tpe}")
              val tv = c.typecheck(v)
              println(s"type of val: ${tv.tpe.widen}")
              q"""case class MyClass()"""
              val f1 = q"""val f1: ${tv.tpe.widen} = $tv"""
              val cc = q"""case class MyClass($f1)"""
              println(s"case class is: ${cc}")

            case _ =>
              println("Unrecognised")
          }
        }).get
      case x: Block =>
        println("found a block")
      case x: Apply =>
        println(s"an apply with?")
      case q"""($e1, $e2)""" =>
        println("got a tuple")
      case q"($k $v)" =>
        println("found tuple")
      case x =>
        println(s"found in block: $x")

    }

    c.prefix.tree match {
      case x =>
        println(s"found: $x")
    }
    c.Expr(
      q"""
         println("Expanded")
         val z = "fish"
         case class ACaseClass()
         println(ACaseClass())
       """
    )
  }
}

trait Common {

  def getFieldNamesAndTypes(c: blackbox.Context)(tpe: c.universe.Type):
  Iterable[(c.universe.Name, c.universe.Type)] = {
    import c.universe._

    object CaseField {
      def unapply(trmSym: TermSymbol): Option[(Name, Type)] = {
        if (trmSym.isVal && trmSym.isCaseAccessor)
          Some((TermName(trmSym.name.toString.trim), trmSym.typeSignature))
        else
          None
      }
    }

    tpe.decls.collect {
      case CaseField(nme, tpe) =>
        (nme, tpe)
    }
  }
}


object FromTuple extends Common {
  implicit def fromTupleMacro[Z, T]: FromTuple[Z, T] = macro fromTupleMacroImpl[Z, T]

  def fromTupleMacroImpl[Z: c.WeakTypeTag, T](c: blackbox.Context): c.Expr[FromTuple[Z, T]] = {
    import c.universe._

    val tpe: Type = weakTypeOf[Z]

    val (nmes, tpes) = getFieldNamesAndTypes(c)(tpe).unzip

    def prj(i: Int): TermName = newTermName("_" + i)

    val prjs = (1 to nmes.toSeq.size).map { i =>
      q"""
       t.${prj(i)}
      """
    }

    val tpeSym: Symbol = tpe.typeSymbol.companion

    val fromTuple: Tree =
      q"""
       new FromTuple[$tpe, (..$tpes)] {
         def fromTuple(t: (..$tpes)) = ${tpeSym}(..$prjs)
       }
      """

    c.Expr[FromTuple[Z, T]](fromTuple)
  }
}