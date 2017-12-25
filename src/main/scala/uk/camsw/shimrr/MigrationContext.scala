package uk.camsw.shimrr

import shapeless.{:+:, ::, CNil, Coproduct, Generic, HList, HNil, Inl, Inr, LabelledGeneric}
import shapeless.labelled.{FieldType, field}
import shapeless.ops.hlist
import shapeless.ops.record.Selector

trait MC[FIELD_DEFAULTS <: HList] {
  type OUT = FIELD_DEFAULTS
  val fieldDefaults: FIELD_DEFAULTS
}

object MC {
  def apply[FIELD_DEFAULTS <: HList](defaults: FIELD_DEFAULTS): MC[FIELD_DEFAULTS] = new MC[FIELD_DEFAULTS] {
    override val fieldDefaults = defaults
  }
}

trait MigrationContext[FIELD_DEFAULTS <: HList] {
  type DEFAULTS = FIELD_DEFAULTS

  val fieldDefaults: DEFAULTS



}

