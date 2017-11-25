package uk.camsw.shimrr

import org.scalatest.Matchers._
import org.scalatest.WordSpec

class MigrateTest extends WordSpec {

  implicit val v1ToV2: Migration[Person, Person_V2] = new Migration[Person, Person_V2] {
    override def migrate(a: Person) = Person_V2(a.name, age = 20)
  }

  "Migrate" should {
    "support addition of a new field on V+1" in {
      val migrated: Person_V2 = Migration.migrate(Person("name"))
      migrated.age shouldBe 20
    }

    "map matching fields to V+1" in {
      val migrated: Person_V2 = Migration.migrate(Person("name"))
      migrated.name shouldBe "name"
    }
  }
}
