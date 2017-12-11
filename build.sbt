name := "shimrr"
organization := "uk.camsw"
scalaVersion := "2.12.4"
version := "1.0.0-SNAPSHOT"

val scalatestVersion = "3.0.4"
val scalacheckVersion = "1.13.5"
val pegdownVersion = "1.6.0"
val shapelessVersion = "2.3.2"
val shapelessScalacheckVersion = "0.6.1"
val catsVersion = "1.0.0-MF"


resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

(testOptions in Test) += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/scalatest-report")
//scalacOptions += "-Yliteral-types"

//inThisBuild(Seq(
//  scalaOrganization := "org.typelevel",
//  scalaVersion := "2.12.4-bin-typelevel-4"
//))
//

val shapelessDependencies: Seq[ModuleID] = Seq(
  "com.chuusai" %% "shapeless" % shapelessVersion,
  "org.typelevel" % "shapeless-scalacheck_2.12" % shapelessScalacheckVersion % "test"
)

val testDependencies: Seq[ModuleID] = Seq(
  "org.pegdown" % "pegdown" % pegdownVersion % "test",
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "org.scalacheck" %% "scalacheck" % scalacheckVersion % "test"
)

val catsDependencies: Seq[ModuleID] = Seq(
  "org.typelevel" % "cats-macros_2.12" % catsVersion,
  "org.typelevel" % "cats-core_2.12" % catsVersion,
  "org.typelevel" % "cats-kernel_2.12" % catsVersion
)

val macroCompatDependencies: Seq[ModuleID] = Seq(
  "org.typelevel" %% "macro-compat" % "1.1.1",
  "org.scala-lang" % "scala-compiler" % "2.12.4" % "provided",
  compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.patch)
)

libraryDependencies ++= catsDependencies
libraryDependencies ++= shapelessDependencies
libraryDependencies ++= macroCompatDependencies
libraryDependencies ++= testDependencies





