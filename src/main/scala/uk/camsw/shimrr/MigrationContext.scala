package uk.camsw.shimrr

import shapeless.HList

trait MigrationContext[FIELD_DEFAULTS <: HList] {
  type OUT = FIELD_DEFAULTS
  val fieldDefaults: FIELD_DEFAULTS
}

private[shimrr] trait GlobalMigrationContext[FIELD_DEFAULTS <: HList] extends MigrationContext[FIELD_DEFAULTS]

private[shimrr] trait ScopedMigrationContext[A <: ReadRepair, FIELD_DEFAULTS <: HList] extends MigrationContext[FIELD_DEFAULTS]

object MigrationContext {

  def global[FIELD_DEFAULTS <: HList](defaults: FIELD_DEFAULTS): GlobalMigrationContext[FIELD_DEFAULTS] = new GlobalMigrationContext[FIELD_DEFAULTS] {
    override val fieldDefaults: FIELD_DEFAULTS = defaults
  }

  class ScopedBuilder[A <: ReadRepair] {
    def apply[FIELD_DEFAULTS <: HList](defaults: FIELD_DEFAULTS): ScopedMigrationContext[A, FIELD_DEFAULTS] = new ScopedMigrationContext[A, FIELD_DEFAULTS] {
      override val fieldDefaults: FIELD_DEFAULTS = defaults
    }
  }

  def scoped[A <: ReadRepair] = new ScopedBuilder[A]
}


