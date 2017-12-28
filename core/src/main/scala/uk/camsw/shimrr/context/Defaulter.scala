package uk.camsw.shimrr.context

trait Defaulter[F] {
  def default: F
}

object Defaulter {
  def instance[F](block: => F): Defaulter[F] = new Defaulter[F] {
    override def default: F = block
  }
}

trait ScopedDefaulter[A, F] {
  def defaultFor(a: A): F
}

object ScopedDefaulter {

  class Builder[A] {
    def apply[F](f: A => F): ScopedDefaulter[A, F] = new ScopedDefaulter[A, F] {
      override def defaultFor(a: A) = f(a)
    }
  }

  def instance[A] = new Builder[A]
}

