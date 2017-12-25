package uk.camsw.shimrr


trait Migration[A, B] {
  def migrate(a: A): B
}

object Migration {

  def instance[A, B <: ReadRepair](f: A => B): Migration[A, B] = (a: A) => f(a)

  def migrate[A <: ReadRepair, B <: ReadRepair](a: A)(implicit m: Migration[A, B]): B = m.migrate(a)

}


