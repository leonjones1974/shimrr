package uk.camsw.shimrr

import uk.camsw.shimrr.macros.FromTuple

object MacroApp extends App {
  def fromTuple[Z: ({ type FromTuple_[Z] = FromTuple[Z, T] })#FromTuple_, T](t: T): Z =
    implicitly[FromTuple[Z, T]].fromTuple(t)

  case class Person(name: String, age: Int)

  val person = new Person("John", 40)


  println(fromTuple[Person, (String, Int)](("John", 40)))
}