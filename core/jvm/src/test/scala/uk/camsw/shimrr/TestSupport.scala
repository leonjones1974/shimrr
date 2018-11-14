package uk.camsw.shimrr

object TestSupport {

  sealed trait Version

  case class Str1Str2Int1(stringField1: String = "str1", stringField2: String = "str2", intField1: Int = 1) extends Version {

    def withNoFields = NoFields()

  }

  case class Str1(stringField1: String) extends Version

  case class Str1Str2(stringField1: String, stringField2: String) extends Version

  case class NoFields() extends Version
}
