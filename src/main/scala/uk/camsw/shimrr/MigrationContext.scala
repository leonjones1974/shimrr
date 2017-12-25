package uk.camsw.shimrr

import shapeless.HList

trait MigrationContext[FIELD_DEFAULTS <: HList] {
  type OUT = FIELD_DEFAULTS
  val fieldDefaults: FIELD_DEFAULTS
}

object MigrationContext {

  def apply[FIELD_DEFAULTS <: HList](defaults: FIELD_DEFAULTS): MigrationContext[FIELD_DEFAULTS] = new MigrationContext[FIELD_DEFAULTS] {
    override val fieldDefaults: FIELD_DEFAULTS = defaults
}}


