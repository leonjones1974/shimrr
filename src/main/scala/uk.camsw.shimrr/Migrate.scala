package uk.camsw.shimrr

trait Migration[A, B] {
  def migrate(a: A): B
}

object Migration {

  def migrate[A <: Product, B <: Product](a: A)(implicit ev: Migration[A, B]): B = ev.migrate(a)

}
