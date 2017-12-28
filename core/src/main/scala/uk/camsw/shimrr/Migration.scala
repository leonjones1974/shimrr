package uk.camsw.shimrr


trait Migration[A, B] {
  def migrate(a: A): B
}

object Migration {

  def instance[A, B](f: A => B): Migration[A, B] = new Migration[A, B]{
    override def migrate(a: A) = f(a)
  }

  def migrate[A, B](a: A)(implicit m: Migration[A, B]): B = m.migrate(a)

}




