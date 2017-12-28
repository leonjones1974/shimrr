package uk.camsw.shimrr

import shapeless.{HList, HNil}
import shapeless.syntax.singleton.mkSingletonOps

trait Dsl {
  type FROM

  private[this] var fieldDefaults: HList = HNil

  implicit class FieldRules[K <: Symbol](k: K) {
    def ~>[V](to: V): Unit = {
      fieldDefaults = (k ->> to) :: fieldDefaults
      println(s"fd: ${fieldDefaults}")
    }
  }


}
