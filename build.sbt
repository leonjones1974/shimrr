name := "shimrr"
organization := "uk.camsw"
scalaVersion := "2.11.8"
version := "1.0.0-SNAPSHOT"

val scalatestVersion = "3.0.4"
val scalacheckVersion = "1.13.5"
val pegdownVersion = "1.6.0"
val shapelessVersion = "2.3.2"


resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

(testOptions in Test) += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/scalatest-report")

val shapelessDependencies: Seq[ModuleID] = Seq(
  "com.chuusai" %% "shapeless" % shapelessVersion
)

val testDependencies: Seq[ModuleID] = Seq(
  "org.pegdown" % "pegdown" % pegdownVersion % "test",
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "org.scalacheck" %% "scalacheck" % scalacheckVersion % "test"
)

libraryDependencies ++= shapelessDependencies
libraryDependencies ++= testDependencies





