package uk.camsw.shimrr

trait Dsl[A] {

}

trait PipelineDsl[A, B, C, D] {

  def from[A](block: => Unit) = Unit
}
