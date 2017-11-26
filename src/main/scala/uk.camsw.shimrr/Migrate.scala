package uk.camsw.shimrr

import shapeless.{:+:, CNil, Coproduct, HList, Inl, Inr, LabelledGeneric}
import shapeless.ops.hlist

trait Migration[A, B] {
  def apply(a: A): B
}

object Migration {

//  implicit class MigrationOps[A](a: A) {
//    def migrateTo[B](implicit migration: Migration[A, B]): B =
//      migration.apply(a)
//  }
//
//  implicit def genericMigration[A, B, ARepr <: HList, BRepr <: HList](
//                                                                       implicit
//                                                                       aGen: LabelledGeneric.Aux[A, ARepr],
//                                                                       bGen: LabelledGeneric.Aux[B, BRepr],
//                                                                       inter: hlist.Intersection.Aux[ARepr, BRepr, BRepr]
//                                                                     ): Migration[A, B] = new Migration[A, B] {
//    def apply(a: A): B = {
//      //      println(s"a is: ${aGen.to(a)}")
//      //      println(s"inter is: ${inter.apply(aGen.to(a))}")
//      bGen.from(inter.apply(aGen.to(a)))
//    }
//  }


}
