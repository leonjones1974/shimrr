package uk.camsw.shimrr.context

import shapeless.HList

trait MigrationContext[FieldDefaults <: HList] {
  val fieldDefaults: FieldDefaults
}



