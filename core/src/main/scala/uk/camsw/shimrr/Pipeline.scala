package uk.camsw.shimrr
trait Pipeline[A, B] {

  def upgrade(from: A): B

}

object Pipeline {

  def instance[FROM, TO](f: FROM => TO): Pipeline[FROM, TO] = (from: FROM) => f(from)


  class PipelineBuilder3[A, B, C] {

    def build(implicit ma: Migration[A, B], mb: Migration[B, C]) = instance[A, C](a => mb.migrate(ma.migrate(a)))

    def to[D] = new PipelineBuilder4[A, B, C, D]
  }

  class PipelineBuilder4[A, B, C, D] {
    def build(implicit ma: Migration[A, B], mb: Migration[B, C], mc: Migration[C, D]) = (
      instance[A, D](a => mc.migrate(mb.migrate(ma.migrate(a)))),
      instance[B, D](b => mc.migrate(mb.migrate(b)))
    )

    def to[E] = new PipelineBuilder5[A, B, C, D, E]
  }

  class PipelineBuilder5[A, B, C, D, E] {
    def build(implicit ma: Migration[A, B], mb: Migration[B, C], mc: Migration[C, D], md: Migration[D, E]) = (
      instance[A, E](a => md.migrate(mc.migrate(mb.migrate(ma.migrate(a))))),
      instance[B, E](b => md.migrate(mc.migrate(mb.migrate(b)))),
      instance[C, E](c => md.migrate(mc.migrate(c)))
    )
  }

  def apply[A, B, C] = new PipelineBuilder3[A, B, C]
  def apply[A, B, C, D] = new PipelineBuilder4[A, B, C, D]
  def apply[A, B, C, D, E] = new PipelineBuilder5[A, B, C, D, E]
}