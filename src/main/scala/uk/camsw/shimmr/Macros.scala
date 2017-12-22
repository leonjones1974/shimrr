package uk.camsw.shimmr

import cats.Monoid

import scala.reflect.macros.blackbox

object Macros {

  def evalImpl(c: blackbox.Context)(expr: c.Expr[Class[_]]): c.Expr[Unit] = {
    import c.universe._
    println(c.eval(expr))
    reify {

      ()
    }
  }


  def evalTImpl[T: c.WeakTypeTag](c: blackbox.Context) = {
    import c.universe._
    val x = c.weakTypeOf[T]
//    c.inferImplicitValue(x, silent = false)
//    import cats.instances._
//    implicitly[Monoid[T]]

    println(s"x: ${x}")
    val t = showRaw(x)
    println(s"t: $t")

    q"""implicitly[Monoid[$x]]"""
  }

  def evalStrImpl[T: c.WeakTypeTag](c: blackbox.Context)(expr: c.Expr[String]) = {
    import c.universe._
    val x = c.weakTypeOf[T]
//    c.inferImplicitValue(x, silent = false)
//    import cats.instances._
//    implicitly[Monoid[T]]

    val ss = c.eval(expr)
    println(s"ss: ${ss}")

    val t = c.mirror.staticClass(ss).toType
    println(s"t: $t")

    q"""implicitly[Monoid[$t]]"""
  }

  import java.net.URL
  import java.net.MalformedURLException
  import scala.reflect.macros.blackbox.Context
  import scala.language.experimental.macros

  object Url {

    import scala.reflect.internal.util.RangePosition
    import language.experimental.macros
    import scala.reflect.internal._

    /**
      * This class contains a macro that will warn you at compile time if you have url format errors
      *
      * use like url"http://www.google.com"
      *
      * @param sc (use the implicit interpolator instead)
      */
    implicit class UrlHelper(val sc: StringContext) extends AnyVal {
      def url(args: Any*): URL = macro UrlHelperimpl
    }


    def UrlHelperimpl(c: Context)(args: c.Expr[Any]*): c.Expr[URL] = {
      import c.universe._

      println("I am here")
      c.prefix.tree match {
        // access data of string interpolation
        case Apply(_, List(Apply(_, rawParts))) =>
          println(s"I am here 2: ${rawParts}")

          // `parts` contain the strings a string interpolation is built of
          val parts = rawParts map { case t@Literal(Constant(const: String)) => (const, t.pos) }
          println(s"parts are: ${parts}")

          parts match {
            case List((raw, pos)) => {
              println("1")
              try {
                println(s"raw: ${raw}")
                val url = new URL(raw)
                println(s"Made url: ${url}")
                //TODO: make a 200 check on this url in UrlLiveValidator?
              } catch {
                case ex: MalformedURLException => {
                  //TODO: could put more work into underlining the problem bits (See the regex validator_
                  c.error(pos, ex.getMessage())
                }

                case ex: Exception => {
                  c.error(pos, "this was a very unexpected error, please file a bug on github (https://github.com/marklemay/scala-validations): " + ex)
                }
              }

              //then parse at compile time
              c.Expr[java.net.URL](q" validation.runtime.Url.parse($raw) ")
            }

            //don't forget the null case
            case List() => {
              println("2")
              c.abort(c.enclosingPosition, "invalid")
            }

            // if there is more then 1 string chunck i.e.   r"regex_${2 + 2}ex"
            // fall back to runtime interpolation
            case _ =>
              println("3")
              c.Expr[java.net.URL](q" validation.runtime.Url.parse(StringContext(..$rawParts), Seq[Any](..$args) ) ")

          }

        case _ =>
          c.abort(c.enclosingPosition, "invalid")
      }
    }
  }

}