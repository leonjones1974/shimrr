package uk.camsw.shimrr.macros


import scala.language.experimental.macros
import scala.reflect.macros.blackbox
trait FromTuple[Z, T] {
  def fromTuple(t: T): Z
}

object Macro {
  def dsl[A](b: => Unit): Any = macro MacroImpl.dsl[A]
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