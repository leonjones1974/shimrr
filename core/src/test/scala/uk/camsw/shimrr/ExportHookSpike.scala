package uk.camsw.shimrr

import export.imports
import org.scalatest.FreeSpec
import export._
import shapeless.HNil
import shapeless.test.illTyped
import uk.camsw.shimrr.macros.sayHello

trait Rules[T] {
  // Type class defns ...
  def saySomething() = println("hello from the summoned rules BOGEY BUM")
}

trait Fish

object Rules extends EncoderLowPriority {
  // Instances which should be higher priority than derived
  // or subclass instances should be defined here ...
}

// Derived, subclass and other instances of Encoder are automatically included here ...
@imports[Rules]
trait EncoderLowPriority {
  // Instances which should be lower priority than imported
  // instances should be defined here ...
}


//@exports
//object DerivedRules {
//  implicit def hnil: DerivedRules[HNil] = new DerivedRules[HNil] {}
//}


class ExportHookSpike extends FreeSpec {

  "we should be able to inject orphan instances" in {
    @sayHello
    trait DerivedRules[String] extends Rules[String] {
      val rules = implicitly[Rules[String]]
      println(rules.saySomething())
    }





    println(s"I am getting: ${DerivedRules.getMe}")
    println(s"with defined val: ${DerivedRules.DerivedRules}")

    //      illTyped{"implicitly[DerivedEncoder[HNil]]"}
//    import DerivedRules.exports._






  }

}
