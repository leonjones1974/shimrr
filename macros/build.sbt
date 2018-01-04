name := "shimrr-macros"
version := "1.0.0-SNAPSHOT"
scalaVersion := "2.12.4"

sourceGenerators in Compile += Def.task {
  val file = (sourceManaged in Compile).value / "uk" / "camsw" / "shimrr" / "dsl" / "PipelineDslGen.scala"

  val newLine = "\r\n"

  val getTraits = for {
    n <- 3 to 22
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
