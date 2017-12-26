package uk.camsw.shimrr

import shapeless.HList

private[shimrr] trait MigrationContext[FIELD_DEFAULTS <: HList] {
  type OUT = FIELD_DEFAULTS
  val fieldDefaults: FIELD_DEFAULTS
}

object MigrationContext {

  def apply[FIELD_DEFAULTS <: HList](defaults: FIELD_DEFAULTS): MigrationContext[FIELD_DEFAULTS] = new MigrationContext[FIELD_DEFAULTS] {
    override val fieldDefaults: FIELD_DEFAULTS = defaults
}}

private[shimrr] trait ScopedMigrationContext[A <: ReadRepair, FIELD_DEFAULTS <: HList] {
  type OUT = FIELD_DEFAULTS
  val fieldDefaults: FIELD_DEFAULTS
}

object ScopedMigrationContext {

  class Builder[A <: ReadRepair] {
    def apply[FIELD_DEFAULTS <: HList](defaults: FIELD_DEFAULTS): ScopedMigrationContext[A, FIELD_DEFAULTS] = new ScopedMigrationContext[A, FIELD_DEFAULTS] {
      override val fieldDefaults: FIELD_DEFAULTS = defaults
    }
  }

  def apply[A <: ReadRepair] = new Builder[A]

}


