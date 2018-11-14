name := "shimrr-core"


publishArtifact in Test := false
publishArtifact in (Compile, packageDoc) := true
publishArtifact in (Compile, packageSrc) := true
publishArtifact := true

val newLine = "\r\n"
sourceGenerators in Compile += Def.task {
  val file = (sourceManaged in Compile).value / "uk" / "camsw" / "shimrr" / "dsl" / "PipelineBuilder.scala"

  val getTraits = for {
    n <- 1 to 22
  } yield {
    val typeNames = for {tn <- 1 to n} yield s"A$tn"
    val types = typeNames.mkString(", ")

    val implicitM = for {tn <- 1 until n} yield
      s"""m$tn: Migration[A$tn, A${tn+1}]"""


    val impls = for {tn <- 0 until n - 2} yield {
      val inType = typeNames(tn)
      val outType = typeNames.last
      val mChain = for {mn <- (1 until n).reverse.dropRight(tn)} yield
        s"""m$mn.migrate"""
      val closeB = List.fill(mChain.length)(")")
      s"""Migration.instance[$inType, $outType](in => ${mChain.mkString("(")}(in)${closeB.mkString("")}"""
    }

    val buildDef = if (implicitM.isEmpty)
      s"""def build"""
    else
      s"""def build(implicit ${implicitM.mkString(", ")})"""


    s"""
       |class PipelineBuilder$n[$types] {
       |  $buildDef = {
       |    (
       |    ${impls.mkString(",\n")}
       |    ${if (impls.length == 1) """ , "dummy" """ else ""}
       |    )
       |  }
       |}
       |""".stripMargin
  }

  val apply = for{
    n <-1 to 22
  } yield {
    val typeNames = for {tn <- 1 to n} yield s"A$tn"
    val types = typeNames.mkString(", ")
    s"""def apply[$types] = new PipelineBuilder$n[$types]"""
  }



  val gen = s"""
               |package uk.camsw.shimrr.dsl
               |import uk.camsw.shimrr.context.Migration
               |${getTraits.mkString(newLine)}
               |
               |object PipelineBuilder {
               |  ${apply.mkString(newLine)}
               |}
   """.stripMargin


  IO.write(file, gen)
  Seq(file)
}.taskValue
