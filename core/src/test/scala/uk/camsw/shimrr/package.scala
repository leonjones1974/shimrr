package uk.camsw

package object shimrr {

  sealed trait Version

  case class Str1Str2Int1(stringField1: String = "str1", stringField2: String = "str2", intField1: Int = 1) extends Version {
    def withoutStringField1 = Str2Int1(stringField2, intField1)

    def withoutStringField2 = Str1Int1(stringField1, intField1)

    def withNoFields = NoFields()

    def withSwappedFields = Str2Int1Str1(stringField2, intField1, stringField1)
  }

  case class Str2Int1Str1(stringField2: String, intField1: Int, stringField1: String) extends Version

  case class Str1(stringField1: String) extends Version

  case class Str2Int1(stringField2: String, intField1: Int) extends Version

  case class Str1Int1(stringField1: String, intField1: Int) extends Version

  case class Int1(intField1: Int) extends Version

  case class Str1Str2(stringField1: String, stringField2: String) extends Version

  case class NoFields() extends Version
}
