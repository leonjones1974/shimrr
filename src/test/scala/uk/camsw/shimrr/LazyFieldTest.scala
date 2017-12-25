package uk.camsw.shimrr

import java.util.concurrent.atomic.AtomicInteger

import org.scalatest.FreeSpec
import shapeless.HNil
import shapeless.syntax.singleton.mkSingletonOps
import syntax._
import org.scalatest.Matchers._

//object LazyFieldRules {
//
//  private val counter = new AtomicInteger(0)
//
//  private def nextCount: () => Int = () => counter.incrementAndGet()
//
//  private[shimrr] val lazyFieldDefaults =
//    'intField1 ->> nextCount ::
//      HNil
//
//}
//
//
//class LazyFieldTest extends FreeSpec
//  with MigrationContext[LazyFieldRules.lazyFieldDefaults.type] {
//
//  override val fieldDefaults = LazyFieldRules.lazyFieldDefaults
//
//  "function can be used to default field" in {
//    val v1 = Str1Str2("str1", "str2").migrateTo[Str1Str2Int1]
//    val v2 = Str1Str2("str1", "str2").migrateTo[Str1Str2Int1]
//
//    v1.intField1 shouldBe 1
//    v2.intField1 shouldBe 2
//  }
//}
