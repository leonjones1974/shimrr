package uk.camsw.shimrr

import macrocompat.bundle

import scala.annotation.{ StaticAnnotation, compileTimeOnly }
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

@compileTimeOnly("enable macro paradise to expand macro annotations")
class migration(enableDebugging: Boolean = false) extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro MacroBundle.dslMigration
}

@compileTimeOnly("enable macro paradise to expand macro annotations")
class pipeline(enableDebugging: Boolean = false) extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro MacroBundle.dslPipeline
}

@bundle
class MacroBundle(val c: whitebox.Context) {

  import c.universe._

  def dslMigration(annottees: Tree*): Tree = {
    import c.universe._

    def abort(msg: String): Unit =
      c.abort(c.enclosingPosition, msg)

    val enableDebugging: Boolean = c.prefix.tree match {
      case q"new migration($b)" => c.eval[Boolean](c.Expr(b))
      case q"new migration(enabledDebugging = $b)" => c.eval[Boolean](c.Expr(b))
      case q"new migration()" => false
    }

    def debug[A](extra: String, a: A): A = {
      if (enableDebugging) println(extra + s" [$a]")
      a
    }

    val migration = annottees match {
      case xs @ List(q"""val $rulesValName = $unused""") =>
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
                          case x @ q"$lhs -> $rhs" =>
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
                        val ctxName = TermName(rulesObjectName.toString.replaceAll("\\.", "_") + "_ctx")
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
        c.abort(
          c.enclosingPosition,
          s"""@migration can only be applied to definition
             |   val xxx = new Dsl[FROM_TYPE] { .. }
             |But was: [$bad]
          """.stripMargin)
        q"()"
    }
    debug("Created migration", migration)
    c.Expr(
      migration).tree
  }

  def dslPipeline(annottees: Tree*): Tree = {
    import c.universe._

    def abort(msg: String): Unit =
      c.abort(c.enclosingPosition, msg)

    val enableDebugging: Boolean = c.prefix.tree match {
      case q"new pipeline($b)" => c.eval[Boolean](c.Expr(b))
      case q"new pipeline()" => false
    }

    def debug[A](extra: String, a: A): A = {
      if (enableDebugging) println(extra + s" [$a]")
      a
    }

    val pipeline = annottees match {
      case xs @ List(q"""val $pipelineName  = $unused""") =>
        xs.head match {
          case q"""val $pipelineName  = $pipelineDef""" =>
            pipelineDef match {
              case q"""new $pipelineDsl[..$pipelineTypes] { ..$body } """ =>
                body match {
                  case dslLines: List[_] =>
                    debug("Found migration rules ", dslLines)
                    val rules = dslLines.collect {
                      case q"from[$from]{ ..$stats }" =>
                        stats match {
                          case dslLines: List[_] =>
                            debug("Found dsl lines", dslLines)

                            val fields = dslLines.map {
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
                              case x @ q"$lhs -> $rhs" =>
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

                            def ensureValid(str: String): String = str.replaceAll("\\.", "_")

                            val ctxName = TermName(ensureValid(from.toString.toLowerCase()) + "Ctx")
                            val rulesName = TypeName(ensureValid(from.toString) + "Rules")
                            val rulesTerm = TermName(ensureValid(from.toString) + "Rules")
                            val rulesGen = TermName(ensureValid(from.toString.toLowerCase) + "_gen")
                            val rulesRepr = TermName(ensureValid(from.toString.toLowerCase) + "_repr")

                            Seq(
                              q"""case class $rulesName(..$fields)""",
                              q"""val $rulesGen = _root_.shapeless.LabelledGeneric[$rulesName]""",
                              q"""val $rulesRepr = $rulesGen.to($rulesTerm())""",
                              q"""implicit val $ctxName = _root_.uk.camsw.shimrr.context.scoped.MigrationContext[$from]($rulesRepr)""")
                        }
                    }

                    val pipelineParts = for {
                      n <- 1 to rules.length - 2
                      name = TermName(s"p$n")
                      part = TermName(s"_$n")
                    } yield q"implicit val $name = _root_.uk.camsw.shimrr.dsl.PipelineBuilder[..$pipelineTypes].build.$part"

                    val exports =
                      q"""
                        object $pipelineName {

                          object exports extends _root_.uk.camsw.shimrr.context.Scoped {
                            ..${rules.flatten};
                            ..$pipelineParts;
                          }
                      }
                    """
                    exports

                }
            }
        }
      case bad =>
        c.abort(
          c.enclosingPosition,
          s"""@migration can only be applied to definition
             |   val xxx = new PipelineDsl<n>[T1, T2, Tn] { .. }
             |But was: [$bad]
          """.stripMargin)
        q"()"
    }

    debug("Created pipeline", pipeline)
    c.Expr(
      pipeline).tree
  }

}

