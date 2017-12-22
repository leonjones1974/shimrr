package uk.camsw.shimrr

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import uk.camsw.shimmr.test.MigrationFreeSpec
import uk.camsw.shimrr.syntax._

trait SameTypeMappingVersionGlobalMigrationRules {

  private[shimrr] val globalFieldDefaults =
    'stringField1 ->> "str1" ::
      'stringField2 ->> "str2" ::
      'intField1 ->> -99 ::
      HNil

  // We must specify the type of our field defaulter
  type FIELD_DEFAULTS = globalFieldDefaults.type
}

class SameTypeMappingTest extends MigrationFreeSpec
  with SameTypeMappingVersionGlobalMigrationRules {

  override val fieldDefaults: FIELD_DEFAULTS = globalFieldDefaults

  "mapping can be performed using normal functor once migration is complete" in {
    val products = Gen.listOfN(1000, Arbitrary.arbitrary[Version])

    GeneratorDrivenPropertyChecks.forAll((versions: List[Version]) => {
      versions.migrateTo[Str1Str2Int1]
        .map(v => v.copy(stringField1 = v.stringField1.toUpperCase))
        .forall(_.stringField1 == "STR1")
    })
  }
}
