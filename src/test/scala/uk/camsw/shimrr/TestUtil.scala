package uk.camsw.shimmr

import org.scalatest._
object TestUtil extends WordSp{


  def assertHasHListRepr[A](a: A): Unit = {
    assertTypeError("""CustomerV1("Leon").migrateTo[CustomerV4]""")
  }


}
