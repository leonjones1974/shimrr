package uk.camsw.shimmr

import scala.annotation.StaticAnnotation
import scala.reflect.macros.blackbox
import scala.language.experimental.macros

class FromSchema(schemaFile: String) extends StaticAnnotation {

  def macroTransform(annottees: Any*) = macro QuasiquotesGenerator.generate

}

//class TestForType[A](schemaFile: String) extends StaticAnnotation {
//  def macroTransform(annottees: Any*) = macro QuasiquotesGenerator.generateTest[A]
//}

case class TypeSchema(name: TypeName, comment: String, fields: Seq[Field])

case class TypeName(fullName: String) {
  /*
    def packageName: String

    def shortName: String*/

}

case class Field(name: String, valueType: TypeName)


object QuasiquotesGenerator {



  def generate(c: blackbox.Context)(annottees: c.Expr[Any]*) = {

    import c.universe._

    // retrieve the schema path
    val schemaPath = c.prefix.tree match {
      case Apply(_, List(Literal(Constant(x)))) => x.toString
      case _ => c.abort(c.enclosingPosition, "schema file path is not specified")
    }
    println(s"Schema path is: ${schemaPath}")

    // retrieve the annotate class name
    val className = annottees.map(_.tree) match {
      case List(q"class $name") => name
      case _ => c.abort(c.enclosingPosition, "the annotation can only be used with classes")
    }

    val imp = q"implicitly[Monoid[$className]]"

    println(s"implicit summonded: ${imp}")

    println(s"className is: ${className}")


    val params = Seq(
      q"val myField: String = $imp"
    )
    //    // load the schema from JSON
    //    val schema = TypeSchema.fromJson(schemaPath)
    //
    //    // produce the list of constructor parameters (note the "val ..." syntax)
    //    val params = schema.fields.map { field =>
    //      val fieldName = newTermName(field.name)
    //      val fieldType = newTypeName(field.valueType.fullName)
    //      q"val $fieldName: $fieldType"
    //    }
    //
    //    val json = TypeSchema.toJson(schema)

    // rewrite the class definition
    //case class $className(..$params) {
    //          def schema = ${json}
    c.Expr(
      q"""
        case class $className(..$params) {
        }
      """
    )
  }

}