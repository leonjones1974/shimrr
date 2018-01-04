package uk.camsw.shimrr

trait Pipeline[A, B] {

  def upgrade(from: A): B

}

object Pipeline {
  def instance[FROM, TO](f: FROM => TO): Pipeline[FROM, TO] = (from: FROM) => f(from)
}

