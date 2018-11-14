package uk.camsw.shimrr.context

import shapeless.{ =:!=, HList, HNil }

object scoped extends ScopedContext {

  trait Scope[S]

  private[shimrr] trait MigrationContext[S, FieldDefaults <: HList] extends Scope[S] {
    val fieldDefaults: FieldDefaults

    class ComposeBuilder[S2, F2 <: HList] {
      def apply[FOut <: HList](ctx2: MigrationContext[S2, F2])(
        implicit
        evNe: S =:!= S2,
        m: shapeless.ops.record.Merger.Aux[FieldDefaults, F2, FOut]): MigrationContext[S, FOut] = {
        MigrationContext[S](m(fieldDefaults, ctx2.fieldDefaults))
      }
    }

    def ++[S2, F2 <: HList] = new ComposeBuilder[S2, F2]

    override def toString: String = s"MigrationContext($fieldDefaults)"
  }

  object MigrationContext {

    class ScopedBuilder[S] {
      def apply[FieldDefaults <: HList](defaults: FieldDefaults = HNil): MigrationContext[S, FieldDefaults] = new MigrationContext[S, FieldDefaults] {
        override val fieldDefaults: FieldDefaults = defaults
      }
    }

    def apply[S]: ScopedBuilder[S] = {
      new ScopedBuilder[S]
    }
  }

}
