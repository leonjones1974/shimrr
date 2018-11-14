
package uk.camsw.shimrr.dsl

import uk.camsw.shimrr._

class PipelineBuilder1[A1] {
  def build = {
    ()
  }
}

class PipelineBuilder2[A1, A2] {
  def build(implicit m1: Migration[A1, A2]) = {
    ()
  }
}

class PipelineBuilder3[A1, A2, A3] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3]) = {
    (
      Migration.instance[A1, A3](in => m2.migrate(m1.migrate(in))), "dummy")
  }
}

class PipelineBuilder4[A1, A2, A3, A4] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4]) = {
    (
      Migration.instance[A1, A4](in => m3.migrate(m2.migrate(m1.migrate(in)))),
      Migration.instance[A2, A4](in => m3.migrate(m2.migrate(in))))
  }
}

class PipelineBuilder5[A1, A2, A3, A4, A5] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5]) = {
    (
      Migration.instance[A1, A5](in => m4.migrate(m3.migrate(m2.migrate(m1.migrate(in))))),
      Migration.instance[A2, A5](in => m4.migrate(m3.migrate(m2.migrate(in)))),
      Migration.instance[A3, A5](in => m4.migrate(m3.migrate(in))))
  }
}

class PipelineBuilder6[A1, A2, A3, A4, A5, A6] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6]) = {
    (
      Migration.instance[A1, A6](in => m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in)))))),
      Migration.instance[A2, A6](in => m5.migrate(m4.migrate(m3.migrate(m2.migrate(in))))),
      Migration.instance[A3, A6](in => m5.migrate(m4.migrate(m3.migrate(in)))),
      Migration.instance[A4, A6](in => m5.migrate(m4.migrate(in))))
  }
}

class PipelineBuilder7[A1, A2, A3, A4, A5, A6, A7] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7]) = {
    (
      Migration.instance[A1, A7](in => m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in))))))),
      Migration.instance[A2, A7](in => m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in)))))),
      Migration.instance[A3, A7](in => m6.migrate(m5.migrate(m4.migrate(m3.migrate(in))))),
      Migration.instance[A4, A7](in => m6.migrate(m5.migrate(m4.migrate(in)))),
      Migration.instance[A5, A7](in => m6.migrate(m5.migrate(in))))
  }
}

class PipelineBuilder8[A1, A2, A3, A4, A5, A6, A7, A8] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8]) = {
    (
      Migration.instance[A1, A8](in => m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in)))))))),
      Migration.instance[A2, A8](in => m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in))))))),
      Migration.instance[A3, A8](in => m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in)))))),
      Migration.instance[A4, A8](in => m7.migrate(m6.migrate(m5.migrate(m4.migrate(in))))),
      Migration.instance[A5, A8](in => m7.migrate(m6.migrate(m5.migrate(in)))),
      Migration.instance[A6, A8](in => m7.migrate(m6.migrate(in))))
  }
}

class PipelineBuilder9[A1, A2, A3, A4, A5, A6, A7, A8, A9] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9]) = {
    (
      Migration.instance[A1, A9](in => m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in))))))))),
      Migration.instance[A2, A9](in => m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in)))))))),
      Migration.instance[A3, A9](in => m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in))))))),
      Migration.instance[A4, A9](in => m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in)))))),
      Migration.instance[A5, A9](in => m8.migrate(m7.migrate(m6.migrate(m5.migrate(in))))),
      Migration.instance[A6, A9](in => m8.migrate(m7.migrate(m6.migrate(in)))),
      Migration.instance[A7, A9](in => m8.migrate(m7.migrate(in))))
  }
}

class PipelineBuilder10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10]) = {
    (
      Migration.instance[A1, A10](in => m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in)))))))))),
      Migration.instance[A2, A10](in => m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in))))))))),
      Migration.instance[A3, A10](in => m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in)))))))),
      Migration.instance[A4, A10](in => m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in))))))),
      Migration.instance[A5, A10](in => m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in)))))),
      Migration.instance[A6, A10](in => m9.migrate(m8.migrate(m7.migrate(m6.migrate(in))))),
      Migration.instance[A7, A10](in => m9.migrate(m8.migrate(m7.migrate(in)))),
      Migration.instance[A8, A10](in => m9.migrate(m8.migrate(in))))
  }
}

class PipelineBuilder11[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11]) = {
    (
      Migration.instance[A1, A11](in => m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in))))))))))),
      Migration.instance[A2, A11](in => m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in)))))))))),
      Migration.instance[A3, A11](in => m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in))))))))),
      Migration.instance[A4, A11](in => m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in)))))))),
      Migration.instance[A5, A11](in => m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in))))))),
      Migration.instance[A6, A11](in => m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in)))))),
      Migration.instance[A7, A11](in => m10.migrate(m9.migrate(m8.migrate(m7.migrate(in))))),
      Migration.instance[A8, A11](in => m10.migrate(m9.migrate(m8.migrate(in)))),
      Migration.instance[A9, A11](in => m10.migrate(m9.migrate(in))))
  }
}

class PipelineBuilder12[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11], m11: Migration[A11, A12]) = {
    (
      Migration.instance[A1, A12](in => m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in)))))))))))),
      Migration.instance[A2, A12](in => m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in))))))))))),
      Migration.instance[A3, A12](in => m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in)))))))))),
      Migration.instance[A4, A12](in => m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in))))))))),
      Migration.instance[A5, A12](in => m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in)))))))),
      Migration.instance[A6, A12](in => m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in))))))),
      Migration.instance[A7, A12](in => m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(in)))))),
      Migration.instance[A8, A12](in => m11.migrate(m10.migrate(m9.migrate(m8.migrate(in))))),
      Migration.instance[A9, A12](in => m11.migrate(m10.migrate(m9.migrate(in)))),
      Migration.instance[A10, A12](in => m11.migrate(m10.migrate(in))))
  }
}

class PipelineBuilder13[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11], m11: Migration[A11, A12], m12: Migration[A12, A13]) = {
    (
      Migration.instance[A1, A13](in => m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in))))))))))))),
      Migration.instance[A2, A13](in => m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in)))))))))))),
      Migration.instance[A3, A13](in => m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in))))))))))),
      Migration.instance[A4, A13](in => m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in)))))))))),
      Migration.instance[A5, A13](in => m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in))))))))),
      Migration.instance[A6, A13](in => m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in)))))))),
      Migration.instance[A7, A13](in => m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(in))))))),
      Migration.instance[A8, A13](in => m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(in)))))),
      Migration.instance[A9, A13](in => m12.migrate(m11.migrate(m10.migrate(m9.migrate(in))))),
      Migration.instance[A10, A13](in => m12.migrate(m11.migrate(m10.migrate(in)))),
      Migration.instance[A11, A13](in => m12.migrate(m11.migrate(in))))
  }
}

class PipelineBuilder14[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11], m11: Migration[A11, A12], m12: Migration[A12, A13], m13: Migration[A13, A14]) = {
    (
      Migration.instance[A1, A14](in => m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in)))))))))))))),
      Migration.instance[A2, A14](in => m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in))))))))))))),
      Migration.instance[A3, A14](in => m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in)))))))))))),
      Migration.instance[A4, A14](in => m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in))))))))))),
      Migration.instance[A5, A14](in => m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in)))))))))),
      Migration.instance[A6, A14](in => m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in))))))))),
      Migration.instance[A7, A14](in => m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(in)))))))),
      Migration.instance[A8, A14](in => m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(in))))))),
      Migration.instance[A9, A14](in => m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(in)))))),
      Migration.instance[A10, A14](in => m13.migrate(m12.migrate(m11.migrate(m10.migrate(in))))),
      Migration.instance[A11, A14](in => m13.migrate(m12.migrate(m11.migrate(in)))),
      Migration.instance[A12, A14](in => m13.migrate(m12.migrate(in))))
  }
}

class PipelineBuilder15[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11], m11: Migration[A11, A12], m12: Migration[A12, A13], m13: Migration[A13, A14], m14: Migration[A14, A15]) = {
    (
      Migration.instance[A1, A15](in => m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in))))))))))))))),
      Migration.instance[A2, A15](in => m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in)))))))))))))),
      Migration.instance[A3, A15](in => m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in))))))))))))),
      Migration.instance[A4, A15](in => m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in)))))))))))),
      Migration.instance[A5, A15](in => m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in))))))))))),
      Migration.instance[A6, A15](in => m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in)))))))))),
      Migration.instance[A7, A15](in => m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(in))))))))),
      Migration.instance[A8, A15](in => m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(in)))))))),
      Migration.instance[A9, A15](in => m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(in))))))),
      Migration.instance[A10, A15](in => m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(in)))))),
      Migration.instance[A11, A15](in => m14.migrate(m13.migrate(m12.migrate(m11.migrate(in))))),
      Migration.instance[A12, A15](in => m14.migrate(m13.migrate(m12.migrate(in)))),
      Migration.instance[A13, A15](in => m14.migrate(m13.migrate(in))))
  }
}

class PipelineBuilder16[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11], m11: Migration[A11, A12], m12: Migration[A12, A13], m13: Migration[A13, A14], m14: Migration[A14, A15], m15: Migration[A15, A16]) = {
    (
      Migration.instance[A1, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in)))))))))))))))),
      Migration.instance[A2, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in))))))))))))))),
      Migration.instance[A3, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in)))))))))))))),
      Migration.instance[A4, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in))))))))))))),
      Migration.instance[A5, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in)))))))))))),
      Migration.instance[A6, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in))))))))))),
      Migration.instance[A7, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(in)))))))))),
      Migration.instance[A8, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(in))))))))),
      Migration.instance[A9, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(in)))))))),
      Migration.instance[A10, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(in))))))),
      Migration.instance[A11, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(in)))))),
      Migration.instance[A12, A16](in => m15.migrate(m14.migrate(m13.migrate(m12.migrate(in))))),
      Migration.instance[A13, A16](in => m15.migrate(m14.migrate(m13.migrate(in)))),
      Migration.instance[A14, A16](in => m15.migrate(m14.migrate(in))))
  }
}

class PipelineBuilder17[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11], m11: Migration[A11, A12], m12: Migration[A12, A13], m13: Migration[A13, A14], m14: Migration[A14, A15], m15: Migration[A15, A16], m16: Migration[A16, A17]) = {
    (
      Migration.instance[A1, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in))))))))))))))))),
      Migration.instance[A2, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in)))))))))))))))),
      Migration.instance[A3, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in))))))))))))))),
      Migration.instance[A4, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in)))))))))))))),
      Migration.instance[A5, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in))))))))))))),
      Migration.instance[A6, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in)))))))))))),
      Migration.instance[A7, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(in))))))))))),
      Migration.instance[A8, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(in)))))))))),
      Migration.instance[A9, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(in))))))))),
      Migration.instance[A10, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(in)))))))),
      Migration.instance[A11, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(in))))))),
      Migration.instance[A12, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(in)))))),
      Migration.instance[A13, A17](in => m16.migrate(m15.migrate(m14.migrate(m13.migrate(in))))),
      Migration.instance[A14, A17](in => m16.migrate(m15.migrate(m14.migrate(in)))),
      Migration.instance[A15, A17](in => m16.migrate(m15.migrate(in))))
  }
}

class PipelineBuilder18[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11], m11: Migration[A11, A12], m12: Migration[A12, A13], m13: Migration[A13, A14], m14: Migration[A14, A15], m15: Migration[A15, A16], m16: Migration[A16, A17], m17: Migration[A17, A18]) = {
    (
      Migration.instance[A1, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in)))))))))))))))))),
      Migration.instance[A2, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in))))))))))))))))),
      Migration.instance[A3, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in)))))))))))))))),
      Migration.instance[A4, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in))))))))))))))),
      Migration.instance[A5, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in)))))))))))))),
      Migration.instance[A6, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in))))))))))))),
      Migration.instance[A7, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(in)))))))))))),
      Migration.instance[A8, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(in))))))))))),
      Migration.instance[A9, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(in)))))))))),
      Migration.instance[A10, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(in))))))))),
      Migration.instance[A11, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(in)))))))),
      Migration.instance[A12, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(in))))))),
      Migration.instance[A13, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(in)))))),
      Migration.instance[A14, A18](in => m17.migrate(m16.migrate(m15.migrate(m14.migrate(in))))),
      Migration.instance[A15, A18](in => m17.migrate(m16.migrate(m15.migrate(in)))),
      Migration.instance[A16, A18](in => m17.migrate(m16.migrate(in))))
  }
}

class PipelineBuilder19[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11], m11: Migration[A11, A12], m12: Migration[A12, A13], m13: Migration[A13, A14], m14: Migration[A14, A15], m15: Migration[A15, A16], m16: Migration[A16, A17], m17: Migration[A17, A18], m18: Migration[A18, A19]) = {
    (
      Migration.instance[A1, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in))))))))))))))))))),
      Migration.instance[A2, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in)))))))))))))))))),
      Migration.instance[A3, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in))))))))))))))))),
      Migration.instance[A4, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in)))))))))))))))),
      Migration.instance[A5, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in))))))))))))))),
      Migration.instance[A6, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in)))))))))))))),
      Migration.instance[A7, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(in))))))))))))),
      Migration.instance[A8, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(in)))))))))))),
      Migration.instance[A9, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(in))))))))))),
      Migration.instance[A10, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(in)))))))))),
      Migration.instance[A11, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(in))))))))),
      Migration.instance[A12, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(in)))))))),
      Migration.instance[A13, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(in))))))),
      Migration.instance[A14, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(in)))))),
      Migration.instance[A15, A19](in => m18.migrate(m17.migrate(m16.migrate(m15.migrate(in))))),
      Migration.instance[A16, A19](in => m18.migrate(m17.migrate(m16.migrate(in)))),
      Migration.instance[A17, A19](in => m18.migrate(m17.migrate(in))))
  }
}

class PipelineBuilder20[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11], m11: Migration[A11, A12], m12: Migration[A12, A13], m13: Migration[A13, A14], m14: Migration[A14, A15], m15: Migration[A15, A16], m16: Migration[A16, A17], m17: Migration[A17, A18], m18: Migration[A18, A19], m19: Migration[A19, A20]) = {
    (
      Migration.instance[A1, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in)))))))))))))))))))),
      Migration.instance[A2, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in))))))))))))))))))),
      Migration.instance[A3, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in)))))))))))))))))),
      Migration.instance[A4, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in))))))))))))))))),
      Migration.instance[A5, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in)))))))))))))))),
      Migration.instance[A6, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in))))))))))))))),
      Migration.instance[A7, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(in)))))))))))))),
      Migration.instance[A8, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(in))))))))))))),
      Migration.instance[A9, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(in)))))))))))),
      Migration.instance[A10, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(in))))))))))),
      Migration.instance[A11, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(in)))))))))),
      Migration.instance[A12, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(in))))))))),
      Migration.instance[A13, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(in)))))))),
      Migration.instance[A14, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(in))))))),
      Migration.instance[A15, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(in)))))),
      Migration.instance[A16, A20](in => m19.migrate(m18.migrate(m17.migrate(m16.migrate(in))))),
      Migration.instance[A17, A20](in => m19.migrate(m18.migrate(m17.migrate(in)))),
      Migration.instance[A18, A20](in => m19.migrate(m18.migrate(in))))
  }
}

class PipelineBuilder21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11], m11: Migration[A11, A12], m12: Migration[A12, A13], m13: Migration[A13, A14], m14: Migration[A14, A15], m15: Migration[A15, A16], m16: Migration[A16, A17], m17: Migration[A17, A18], m18: Migration[A18, A19], m19: Migration[A19, A20], m20: Migration[A20, A21]) = {
    (
      Migration.instance[A1, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in))))))))))))))))))))),
      Migration.instance[A2, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in)))))))))))))))))))),
      Migration.instance[A3, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in))))))))))))))))))),
      Migration.instance[A4, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in)))))))))))))))))),
      Migration.instance[A5, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in))))))))))))))))),
      Migration.instance[A6, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in)))))))))))))))),
      Migration.instance[A7, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(in))))))))))))))),
      Migration.instance[A8, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(in)))))))))))))),
      Migration.instance[A9, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(in))))))))))))),
      Migration.instance[A10, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(in)))))))))))),
      Migration.instance[A11, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(in))))))))))),
      Migration.instance[A12, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(in)))))))))),
      Migration.instance[A13, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(in))))))))),
      Migration.instance[A14, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(in)))))))),
      Migration.instance[A15, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(in))))))),
      Migration.instance[A16, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(in)))))),
      Migration.instance[A17, A21](in => m20.migrate(m19.migrate(m18.migrate(m17.migrate(in))))),
      Migration.instance[A18, A21](in => m20.migrate(m19.migrate(m18.migrate(in)))),
      Migration.instance[A19, A21](in => m20.migrate(m19.migrate(in))))
  }
}

class PipelineBuilder22[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22] {
  def build(implicit m1: Migration[A1, A2], m2: Migration[A2, A3], m3: Migration[A3, A4], m4: Migration[A4, A5], m5: Migration[A5, A6], m6: Migration[A6, A7], m7: Migration[A7, A8], m8: Migration[A8, A9], m9: Migration[A9, A10], m10: Migration[A10, A11], m11: Migration[A11, A12], m12: Migration[A12, A13], m13: Migration[A13, A14], m14: Migration[A14, A15], m15: Migration[A15, A16], m16: Migration[A16, A17], m17: Migration[A17, A18], m18: Migration[A18, A19], m19: Migration[A19, A20], m20: Migration[A20, A21], m21: Migration[A21, A22]) = {
    (
      Migration.instance[A1, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(m1.migrate(in)))))))))))))))))))))),
      Migration.instance[A2, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(m2.migrate(in))))))))))))))))))))),
      Migration.instance[A3, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(m3.migrate(in)))))))))))))))))))),
      Migration.instance[A4, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(m4.migrate(in))))))))))))))))))),
      Migration.instance[A5, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(m5.migrate(in)))))))))))))))))),
      Migration.instance[A6, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(m6.migrate(in))))))))))))))))),
      Migration.instance[A7, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(m7.migrate(in)))))))))))))))),
      Migration.instance[A8, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(m8.migrate(in))))))))))))))),
      Migration.instance[A9, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(m9.migrate(in)))))))))))))),
      Migration.instance[A10, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(m10.migrate(in))))))))))))),
      Migration.instance[A11, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(m11.migrate(in)))))))))))),
      Migration.instance[A12, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(m12.migrate(in))))))))))),
      Migration.instance[A13, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(m13.migrate(in)))))))))),
      Migration.instance[A14, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(m14.migrate(in))))))))),
      Migration.instance[A15, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(m15.migrate(in)))))))),
      Migration.instance[A16, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(m16.migrate(in))))))),
      Migration.instance[A17, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(m17.migrate(in)))))),
      Migration.instance[A18, A22](in => m21.migrate(m20.migrate(m19.migrate(m18.migrate(in))))),
      Migration.instance[A19, A22](in => m21.migrate(m20.migrate(m19.migrate(in)))),
      Migration.instance[A20, A22](in => m21.migrate(m20.migrate(in))))
  }
}

object PipelineBuilder {
  def apply[A1] = new PipelineBuilder1[A1]
  def apply[A1, A2] = new PipelineBuilder2[A1, A2]
  def apply[A1, A2, A3] = new PipelineBuilder3[A1, A2, A3]
  def apply[A1, A2, A3, A4] = new PipelineBuilder4[A1, A2, A3, A4]
  def apply[A1, A2, A3, A4, A5] = new PipelineBuilder5[A1, A2, A3, A4, A5]
  def apply[A1, A2, A3, A4, A5, A6] = new PipelineBuilder6[A1, A2, A3, A4, A5, A6]
  def apply[A1, A2, A3, A4, A5, A6, A7] = new PipelineBuilder7[A1, A2, A3, A4, A5, A6, A7]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8] = new PipelineBuilder8[A1, A2, A3, A4, A5, A6, A7, A8]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9] = new PipelineBuilder9[A1, A2, A3, A4, A5, A6, A7, A8, A9]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10] = new PipelineBuilder10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11] = new PipelineBuilder11[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12] = new PipelineBuilder12[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13] = new PipelineBuilder13[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14] = new PipelineBuilder14[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15] = new PipelineBuilder15[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16] = new PipelineBuilder16[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17] = new PipelineBuilder17[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18] = new PipelineBuilder18[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19] = new PipelineBuilder19[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20] = new PipelineBuilder20[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21] = new PipelineBuilder21[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21]
  def apply[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22] = new PipelineBuilder22[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22]
}
