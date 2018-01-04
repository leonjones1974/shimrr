package uk.camsw.shimrr

trait MigrationDsl[A] {

}

trait PipelineDsl[A1, A2, A3, A4] {
  val exports: Unit = ()
  def from[IN](block: => Unit): Unit = Unit
}
