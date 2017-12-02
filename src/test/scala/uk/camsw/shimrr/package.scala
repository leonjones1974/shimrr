package uk.camsw

package object shimrr {

  sealed trait Version

  case class BaseVersion(stringField1: String = "str1", stringField2: String = "str2", intField1: Int = 1) extends Version {
    def withoutStringField1 = VersionWithoutStringField1(stringField2, intField1)

    def withoutStringField2 = VersionWithoutStringField2(stringField1, intField1)

    def withNoFields = VersionWithNoFields()

    def withSwappedFields = VersionWithSwappedFields(stringField2, intField1, stringField1)
  }

  case class VersionWithSwappedFields(stringField2: String, intField1: Int, stringField1: String) extends Version

  case class VersionWithoutStringField1(stringField2: String, intField1: Int) extends Version

  case class VersionWithoutStringField2(stringField1: String, intField1: Int) extends Version

  case class VersionWithoutIntField1(stringField1: String, stringField2: String) extends Version

  case class VersionWithNoFields() extends Version

}
