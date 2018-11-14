package uk.camsw.shimrr
import org.scalatest.FreeSpec
import org.scalatest.Matchers._
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimrr.syntax._

class MapFieldTypeTest extends FreeSpec {

  "field types can be changed" in {

    sealed trait Versioned
    case class V1(name: String, price: Float) extends Versioned
    case class V2(name: String, price: BigDecimal) extends Versioned

    import uk.camsw.shimrr.context.scoped._

    val v1Price: V1 => BigDecimal = p => BigDecimal(p.price.toString)

    implicit val v1Rules = MigrationContext[V1](
      'price ->> v1Price :: HNil)

    implicit val v2Rules = MigrationContext[V2]()

    val xs = List[Versioned](
      V1("Leon Jones", 1.50f),
      V2("Nicky Hayden", BigDecimal("1.25")))

    xs.migrateTo[V2] shouldBe List(
      V2("Leon Jones", BigDecimal("1.50")),
      V2("Nicky Hayden", BigDecimal("1.25")))
  }
}
