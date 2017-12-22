package uk.camsw.shimrr

trait Defaulter[A] {
  def empty: A
}

object Defaulter {
  def instance[A](a: => A): Defaulter[A] = new Defaulter[A] {
    override def empty: A = a
  }
}
