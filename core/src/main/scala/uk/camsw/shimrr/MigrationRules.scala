package uk.camsw.shimrr

import shapeless.HList

import scala.reflect.ClassTag

class MigrationRules[S](defaults: HList)(implicit tt: ClassTag[S]) {
  val scope: String = tt.runtimeClass.getName
}

class Dsl2() {
  //  type Scope = S
}