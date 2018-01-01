package uk.camsw.shimrr

trait Pipeline[A, B] {

  def upgrade(from: A): B

}

private object p3 { implicit val dummy: p3.type = this }
private object p4 { implicit val dummy: p4.type = this }

object Pipeline {

  def instance[FROM, TO](f: FROM => TO): Pipeline[FROM, TO] = (from: FROM) => f(from)

  class PipelineBuilder3[A, B, C] {

    def build(ma: Migration[A, B], mb: Migration[B, C]) = (
      instance[A, B](ma.migrate),
      instance[B, C](mb.migrate)
    )

    def ++[D] = new PipelineBuilder4[A, B, C, D]
  }

  class PipelineBuilder4[A, B, C, D] {
    def build(implicit ma: Migration[A, B], mb: Migration[B, C], mc: Migration[C, D]) = (
      instance[A, D](a => mc.migrate(mb.migrate(ma.migrate(a)))),
      instance[B, D](b => mc.migrate(mb.migrate(b)))
    )
  }

  def apply[A, B, C](implicit ev: p3.type) = new PipelineBuilder3[A, B, C]
  def apply[A, B, C, D](implicit ev: p4.type) = new PipelineBuilder4[A, B, C, D]
}