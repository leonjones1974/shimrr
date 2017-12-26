package uk.camsw.shimrr

trait Defaulter[F] {
  def empty: F
}

object Defaulter {
  def instance[F](block: => F): Defaulter[F] = new Defaulter[F] {
    override def empty: F = block
  }
}

trait ScopedDefaulter[A, F] {
  def empty: F
}

object ScopedDefaulter {

  class Builder[A] {
    def apply[F](block: F): ScopedDefaulter[A, F] = new ScopedDefaulter[A, F] {
      override def empty = block
    }
  }

  def instance[A] = new Builder[A]
}

