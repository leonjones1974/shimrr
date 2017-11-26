package uk.camsw.shimrr

import org.scalatest.Matchers._
import org.scalatest.WordSpec
import shapeless.ops.hlist
import shapeless.{Generic, HList, HNil, LabelledGeneric, Poly1}
import Migration._
import shapeless._

import scala.reflect.ClassTag

class MigrateTest extends WordSpec {

  //  implicit val v1ToV2: Migration[Person, Person_V2] = new Migration[Person, Person_V2] {
  //    override def migrate(a: Person) = Person_V2(a.name, age = 20)
  //  }


  "migrateTo" should {
    //    "support addition of a new field on V+1" in {
    //      val migrated: Person_V2 = Migration.migrate(Person("name"))
    //      migrated.age shouldBe 20
    //    }
    //
    //    "map matching fields to V+1" in {
    //      val migrated: Person_V2 = Migration.migrate(Person("name"))
    //      migrated.name shouldBe "name"
    //    }

    "support removal of a field" in {
      //      val migrated = Person_V1("Leon", "Jones", 20).migrateTo[Person_V2]
      //      migrated shouldBe Person_V2("Leon", "Jones")
    }
  }
  //
  //  object poly extends Poly1 {
  //    implicit def person1Type =
  //      at[Person_V1.type] { _ => println("person") }
  //
  //    implicit def person2Type =
  //      at[Person_V2.type] { _ => println("person2") }
  //
  //    implicit def person3Type =
  //      at[Person_V3.type] { _ => println("person3") }
  //  }
  //

  "upgrade" should {
    "apply a chain of version migrations in order" in {


      val versions = Person_V1 :: Person_V2 :: Person_V3 :: HNil

      val toUpgrade: List[Person] = List(Person_V1("Leon", "Jones", 21), Person_V2("fred", "basset"), Person_V1("Spencer", "Ward", 56), Person_V3("Bonnie"))

      implicit def cnilMigration: Migration[CNil, Person_V3] = new Migration[CNil, Person_V3] {
        override def apply(a: CNil) = throw new Exception("Inconceivable!")
      }

      implicit def genericMigration[A, B, ARepr <: HList, BRepr <: HList](
                                                                           implicit
                                                                           aGen: LabelledGeneric.Aux[A, ARepr],
                                                                           bGen: LabelledGeneric.Aux[B, BRepr],
                                                                           inter: hlist.Intersection.Aux[ARepr, BRepr, BRepr]
                                                                         ): Migration[A, B] = new Migration[A, B] {
        def apply(a: A): B = {
          //      println(s"a is: ${aGen.to(a)}")
          //      println(s"inter is: ${inter.apply(aGen.to(a))}")
          println(s"Using generic migration: a=${a}")
          bGen.from(inter.apply(aGen.to(a)))
        }
      }

      implicit def genericXXX[A, B](
                                         implicit
                                         gen: Generic[A] {type Repr = B},
                                         mig: Migration[A, B]
                                       ): Migration[A, B] = ???



      implicit def coproductMigration[H, T <: Coproduct](): Migration[H :+: T, Any] = new Migration[H :+: T, Any] {
        override def apply(a: :+:[H, T]) = ??? // a match {
        //          case Inl(h) => hMig.value(h)
        //          case Inr(t) => tMig(t)
        //        }
      }

      val p1: Person = Person_V1("Leon", "Jones", 21)
      val p2: Person = Person_V2("Leon", "Jones")
//      val xxx: Person_V3 = migrate(p1)

      def migrate[A, B](a: A)(implicit ev: Migration[A, B]): B =
        ev(a)

      def migrateAll[A, B](xs: List[A])(implicit ev: Migration[A, B]): List[B] =
        xs.map(v => {
          println(s"Migrating v: ${v}")
          ev(v)
        }
        )

//      val r: List[Person_V3] = migrateAll[Person_V1, Person_V3](List(
//        Person_V1("a", "b", 23),
//        Person_V1("a", "b", 25))
//      )

//      val p3: Person_V3 = migrate[Person_V1, Person_V3](Person_V1("a", "b", 23))

      //      val gen = Generic[Person]
      //      val x = gen.to(p1)
      //      println(s"x: ${x}")

      //      val v3: Person_V3 = p1.migrateTo[Person_V3]

      //      val person_V3: Person_V3 = migrate[Person, Person_V3](p1)
      //      person_V3 shouldBe Person_V3("Leon")


      //      v3 shouldBe Person_V3("Leon")
      //
      //      println(s"init: ${versions.init}")
      //      println(s"tail: ${versions.tail}")
      //      println(s"select: ${versions.select[Person_V1.type]}")
      //
      //      //      def findVersion[A <: Person_V1, ARepr <: HList](a: A)(
      //        implicit
      //        tag: ClassTag[A],
      //        genA: Generic.Aux[A, ARepr]): Any = {
      //        versions.select[tag.type]
      //      }

    }
  }

  "book  example" should {
    "work" in {
      sealed trait Shape
      final case class Triangle(x: Int, y: Int, z: Int) extends Shape
      final case class Rectangle(width: Double, height: Double) extends Shape
      final case class Circle(radius: Double) extends Shape

      import shapeless.{HList, ::, HNil}
      trait CsvEncoder[A] {
        def encode(value: A): List[String]
      }

      def createEncoder[A](func: A => List[String]): CsvEncoder[A] =
        new CsvEncoder[A] {
          def encode(value: A): List[String] = func(value)
        }

      implicit val stringEncoder: CsvEncoder[String] =
        createEncoder(str => List(str))
      implicit val intEncoder: CsvEncoder[Int] =
        createEncoder(num => List(num.toString))
      implicit val booleanEncoder: CsvEncoder[Boolean] =
        createEncoder(bool => List(if (bool) "yes" else "no"))
      implicit val doubleEncoder: CsvEncoder[Double] =
        createEncoder(d => List(d.toString))
      implicit val hnilEncoder: CsvEncoder[HNil] =
        createEncoder(hnil => Nil)

      implicit def hlistEncoder[H, T <: HList](
                                                implicit
                                                hEncoder: CsvEncoder[H],
                                                tEncoder: CsvEncoder[T]
                                              ): CsvEncoder[H :: T] =
        createEncoder {
          case h :: t =>
            println(s"using hlist encoder: (${h}) :: ${t}")
            hEncoder.encode(h) ++ tEncoder.encode(t)
        }

      implicit def genericEncoder[A, R](
                                         implicit
                                         gen: Generic[A] {type Repr = R},
                                         enc: CsvEncoder[R]
                                       ): CsvEncoder[A] =
        createEncoder(a => {
          println(s"Using generic encoder: ${a}")
          enc.encode(gen.to(a))
        })

      def writeCsv[A](values: List[A])(implicit enc: CsvEncoder[A]): String =
        values.map(value => enc.encode(value).mkString(",")).mkString("\n")

      def write[A](value: A)(implicit enc: CsvEncoder[A]): String =
        enc.encode(value).mkString(",")

      implicit val cnilEncoder: CsvEncoder[CNil] =
        createEncoder(cnil => throw new Exception("Inconceivable!"))

      implicit def coproductEncoder[H, T <: Coproduct](
                                                        implicit
                                                        hEncoder: CsvEncoder[H],
                                                        tEncoder: CsvEncoder[T]
                                                      ): CsvEncoder[H :+: T] = createEncoder {
        case Inl(h) =>
          println(s"coprod: h=${h}")
          hEncoder.encode(h)
        case Inr(t) =>
          println(s"coprod: t=${t}")
          tEncoder.encode(t)
      }

      def migrate[A, B](a: A)(implicit ev: Migration[A, B]): B =
        ev(a)

      //      val person_V3: Person_V3 = migrate[Rectangle, Person_V3](Rectangle(1, 2))
//      val r1: Shape = Rectangle(2d, 3d)
      //      val csv = writeCsv(List(r1, Triangle(1, 2, 3)))
      val xs: List[Person] = List(Person_V1("a", "n", 3), Person_V2("a", "b"))
      val csv2 = writeCsv(xs)
      println(s"csv: ${csv2}")
      writeCsv(List("fish" :: HNil))

    }
  }


  "some other" should {
    "fucking work" in {

    }
  }
}
