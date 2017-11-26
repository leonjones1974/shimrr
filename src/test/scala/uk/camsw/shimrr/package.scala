package uk.camsw

package object shimrr {

  sealed trait Version

  final case class BaseVersion(stringField1: String = "str1", stringField2: String = "str2", intField1: Int = 1) extends Version {
    def withoutStringField1 = VersionWithoutStringField1(stringField2, intField1)
    def withoutStringField2 = VersionWithoutStringField2(stringField1, intField1)
    def withNoFields = VersionWithNoFields()
  }

  final case class VersionWithoutStringField1(stringField2: String, intField1: Int) extends Version

  final case class VersionWithoutStringField2(stringField1: String, intField1: Int) extends Version

  final case class VersionWithoutIntField1(stringField1: String, stringField2: String) extends Version

  final case class VersionWithNoFields() extends Version

}
