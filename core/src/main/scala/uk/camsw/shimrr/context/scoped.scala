package uk.camsw.shimrr.context

import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist
import shapeless.ops.record.Selector
import shapeless.{:+:, ::, =:!=, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric, Lazy}
import uk.camsw.shimrr.{Migration, Pipeline}

object scoped {

  trait Scope[S]

  private[shimrr] trait MigrationContext[S, FieldDefaults <: HList] extends Scope[S] {
    val fieldDefaults: FieldDefaults

    class ComposeBuilder[S2, F2 <: HList] {
      def apply[FOut <: HList](ctx2: MigrationContext[S2, F2])(
        implicit
        evNe: S =:!= S2,
        m: shapeless.ops.record.Merger.Aux[FieldDefaults, F2, FOut]
      ): MigrationContext[S, FOut] = {
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


  implicit def fromCNil[T <: CNil, B]: Migration[T, B] =
    Migration.instance(a =>
      throw new RuntimeException(s"Will not happen, but did for $a"))


  implicit def fromCoproduct[A, ARepr <: Coproduct, B](
                                                        implicit
                                                        genA: Generic.Aux[A, ARepr],
                                                        m: Migration[ARepr, B]): Migration[A, B] =
    Migration.instance(a =>
      m.migrate(genA.to(a))
    )


  implicit def fromCoproductRepr[H, T <: Coproduct, B](
                                                        implicit
                                                        mH: Lazy[Migration[H, B]],
                                                        mT: Migration[T, B]
                                                      ): Migration[H :+: T, B] =
    Migration.instance {
      case Inl(h) =>
        mH.value.migrate(h)
      case Inr(t) =>
        mT.migrate(t)
    }


  implicit def fromProduct[
  A, ARepr <: HList,
  B, BRepr <: HList,
  Common <: HList,
  Added <: HList,
  Unaligned <: HList,
  FieldDefaults <: HList](
                           implicit
                           scope: Scope[A],
                           genA: LabelledGeneric.Aux[A, ARepr],
                           genB: LabelledGeneric.Aux[B, BRepr],
                           inter: hlist.Intersection.Aux[ARepr, BRepr, Common],
                           diff: hlist.Diff.Aux[BRepr, Common, Added],
                           defaulter: ScopedDefaulter[A, Added],
                           prepend: hlist.Prepend.Aux[Added, Common, Unaligned],
                           align: hlist.Align[Unaligned, BRepr]
                         ): Migration[A, B] =
    Migration.instance {
      a =>
        genB.from(align(prepend(defaulter.defaultFor(a), inter(genA.to(a)))))

    }

    implicit def fromProductPipeline[
      A, ARepr <: HList,
      B, BRepr <: HList
    ](implicit pipeline: Pipeline[A, B]) : Migration[A, B] = {
      Migration.instance{
        a =>
          pipeline.upgrade(a)
      }
    }

//  implicit def fromProductPoly[
//  S,
//  A, ARepr <: HList,
//  B, BRepr <: HList,
//  Common <: HList,
//  Added <: HList,
//  Unaligned <: HList,
//  FieldDefaults <: HList](
//                           implicit
//                           scope: Scope[S],
//                           scopeEv: A <:< S,
//                           notEq: A =:!= S,
//                           genA: LabelledGeneric.Aux[A, ARepr],
//                           genB: LabelledGeneric.Aux[B, BRepr],
//                           inter: hlist.Intersection.Aux[ARepr, BRepr, Common],
//                           diff: hlist.Diff.Aux[BRepr, Common, Added],
//                           defaulter: ScopedDefaulter[S, Added],
//                           prepend: hlist.Prepend.Aux[Added, Common, Unaligned],
//                           align: hlist.Align[Unaligned, BRepr]
//                         ): Migration[A, B] =
//    Migration.instance {
//      a =>
//        genB.from(align(prepend(defaulter.defaultFor(a), inter(genA.to(a)))))
//
//    }

  implicit def literalDefaulter[S, FieldDefaults <: HList, K <: Symbol, H, T <: HList](
                                                                                        implicit
                                                                                        ctx: MigrationContext[S, FieldDefaults],
                                                                                        selector: Selector.Aux[FieldDefaults, K, H],
                                                                                        dT: ScopedDefaulter[S, T]
                                                                                      ): ScopedDefaulter[S, FieldType[K, H] :: T] =
    ScopedDefaulter.instance[S] { a =>
      field[K](selector(ctx.fieldDefaults)) :: field[K](dT.defaultFor(a))
    }

  implicit def lazyDefaulter[S, FieldDefaults <: HList, K <: Symbol, H, T <: HList](
                                                                                     implicit
                                                                                     ctx: MigrationContext[S, FieldDefaults],
                                                                                     selector: Selector.Aux[FieldDefaults, K, () => H],
                                                                                     dT: ScopedDefaulter[S, T]
                                                                                   ): ScopedDefaulter[S, FieldType[K, H] :: T] =
    ScopedDefaulter.instance[S] { a =>
      field[K](selector(ctx.fieldDefaults)()) :: field[K](dT.defaultFor(a))
    }

  implicit def parameterizedDefaulter[S, FieldDefaults <: HList, K <: Symbol, H, T <: HList](
                                                                                              implicit
                                                                                              ctx: MigrationContext[S, FieldDefaults],
                                                                                              selector: Selector.Aux[FieldDefaults, K, S => H],
                                                                                              dT: ScopedDefaulter[S, T]
                                                                                            ): ScopedDefaulter[S, FieldType[K, H] :: T] = {
    ScopedDefaulter.instance[S] { a =>
      field[K](selector(ctx.fieldDefaults)(a)) :: field[K](dT.defaultFor(a))
    }
  }

  implicit def fromHNil[S, FieldDefaults <: HList]: ScopedDefaulter[S, HNil] =
    ScopedDefaulter.instance(_ => HNil)
}
