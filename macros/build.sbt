name := "shimrr-macros"

publishArtifact in Test := false
publishArtifact in (Compile, packageDoc) := true
publishArtifact in (Compile, packageSrc) := true
publishArtifact := true

val newLine = "\r\n"

sourceGenerators in Compile += Def.task {
  val file = (sourceManaged in Compile).value / "uk" / "camsw" / "shimrr" / "dsl" / "PipelineDslGen.scala"


  val getTraits = for {
    n <- 1 to 22
  } yield {
    val typeNames = for {tn <- 1 to n} yield s"A$tn"
    val types = typeNames.mkString(", ")
    s"""
       |trait PipelineDsl$n[$types] {
       |  val exports: Unit = ()
       |  def from[IN](block: => Unit): Unit = Unit
       |}""".stripMargin

  }


  val gen = s"""
               |package uk.camsw.shimrr.dsl
               |${getTraits.mkString(newLine)}
   """.stripMargin


  IO.write(file, gen)
  Seq(file)
}.taskValue

