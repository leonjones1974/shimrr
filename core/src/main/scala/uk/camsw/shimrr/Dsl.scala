package uk.camsw.shimrr

trait Dsl[A] {

}

trait PipelineDsl[A, B, C, D] {

  val exports: Unit = ()

  def from[A](block: => Unit) = Unit
}
