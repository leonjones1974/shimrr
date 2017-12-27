package uk.camsw.shimrr.context

import shapeless.HList

trait MigrationContext[FIELD_DEFAULTS <: HList] {
  type OUT = FIELD_DEFAULTS
  val fieldDefaults: FIELD_DEFAULTS
}



