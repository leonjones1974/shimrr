package uk.camsw

package object shimrr {

  sealed trait Person

  final case class Person_V1(firstName: String, surname: String, age: Int) extends Person

  final case class Person_V2(firstName: String, surname: String) extends Person

  final case class Person_V3(firstName: String) extends Person


}
