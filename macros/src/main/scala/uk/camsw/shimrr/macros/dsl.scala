package uk.camsw.shimrr.macros


import macrocompat.bundle

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.whitebox


@compileTimeOnly("enable macro paradise to expand macro annotations")
class migration extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro MacroBundle.dslF
}

@bundle
class MacroBundle(val c: whitebox.Context) {
  val enableDebugging = false

  import c.universe._

  def dslF(annottees: Tree*): Tree = {
    import c.universe._

    def abort(msg: String): Unit =
      c.abort(c.enclosingPosition, msg)


    def debug[A](extra: String, a: A): A = {
      if (enableDebugging) println(extra + s" [$a]")
      a
    }

    val migration = annottees match {
      case xs@List(q"""val $rulesValName = $unused""") =>
        val rulesObjectName = TermName(rulesValName.toString())
        debug("Attempting to expand", rulesObjectName)

        xs.head match {
          case q"""val $migrationName = $anonDslImpl""" =>
            debug("Creating migration", migrationName)
            anonDslImpl match {
              case q"new { ..$unused } with $init { $unused1 => ..$stats }" =>
                debug("Found anonymous dsl", init)
                init match {
                  case tq"$dsl[$typ]" =>
                    debug("Found dsl for type", typ)
                    stats match {
                      case dslLines: List[_] =>
                        debug("Found dsl lines", dslLines)

                        val fields = dslLines.collect {
                          case Apply(_, List(sym, v)) =>
                            val fieldName = TermName(sym match {
                              case q"""scala.Symbol($symbolName)""" =>
                                symbolName.toString().drop(1).dropRight(1)
                              case x =>
                                c.abort(c.enclosingPosition, s"Unable to extract field name from: [$x]")
                            })
                            debug("Found field name", fieldName)

                            val fieldValue = c.typecheck(v)
                            debug("Identified type of field", fieldValue.tpe.widen)

                            q"""val $fieldName: ${fieldValue.tpe.widen} = $fieldValue"""
                          case x@q"$lhs -> $rhs" =>
                            //TODO: Dry this up
                            debug("Identified arrow style field defaulter", x)
                            val fieldName = TermName(lhs match {
                              case q"""scala.Symbol($symbolName)""" =>
                                symbolName.toString().drop(1).dropRight(1)
                              case x =>
                                c.abort(c.enclosingPosition, s"Unable to extract field name from: [$x]")
                            })
                            debug("Found field name", fieldName)

                            val fieldValue = c.typecheck(rhs)
                            debug("Identified type of field", fieldValue.tpe.widen)

                            q"""val $fieldName: ${fieldValue.tpe.widen} = $fieldValue"""

                        }
                        val rulesDefinition = q"""case class RuleDefinition(..$fields)"""
                        val ctxName = TermName(rulesObjectName.toString + "_ctx")
                        debug("Exporting migration context as", ctxName)
                        q"""
                            object $rulesObjectName {
                              $rulesDefinition
                              val gen = _root_.shapeless.LabelledGeneric[RuleDefinition]
                              val repr = gen.to(RuleDefinition())

                              override def toString() = RuleDefinition().toString
                              val definition = RuleDefinition()


                              object exports {
                                ..$stats;
                                implicit val $ctxName = _root_.uk.camsw.shimrr.context.scoped.MigrationContext[$typ](repr)
                            }
                          }
                        """
                    }
                }
            }
        }
      case bad =>
        c.abort(c.enclosingPosition,
          s"""@migration can only be applied to definition
             |   class SomeClass[FROM_TYPE] { .. }
             |But was: [$bad]
          """.stripMargin)
        q"()"
    }
    debug("Created migration", migration)
    c.Expr(
      migration
    ).tree
  }
}
