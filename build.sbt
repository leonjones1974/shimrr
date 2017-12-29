name in ThisBuild := "shimrr"
organization in ThisBuild := "uk.camsw"
scalaVersion in ThisBuild := "2.12.4"
version in ThisBuild := "1.0.0-SNAPSHOT"

val scalatestVersion = "3.0.4"
val scalacheckVersion = "1.13.5"
val pegdownVersion = "1.6.0"
val shapelessVersion = "2.3.2"
val shapelessScalacheckVersion = "0.6.1"
val catsVersion = "1.0.0-MF"
val paradisePluginVersion = "3.0.0-M10"
val scalametaVersion = "1.8.0"

(testOptions in Test) += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/scalatest-report")
logBuffered in Test := false

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.bintrayRepo("scalameta", "maven")


lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  addCompilerPlugin("io.tryp" % "splain" % "0.2.7" cross CrossVersion.patch),
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  scalacOptions in(Compile, console) := Seq() // macroparadise plugin doesn't work in repl yet.
)


lazy val macros = project.settings(
  metaMacroSettings
).settings(libraryDependencies ++= scalameta ++ reflectionDependencies ++ shapelessDependencies)


lazy val core = project
  .settings(metaMacroSettings)
  .dependsOn(macros)
  .settings(libraryDependencies ++= catsDependencies ++ shapelessDependencies ++ testDependencies)

lazy val tutorials = project.settings(
  metaMacroSettings)
  .dependsOn(core)

val scalameta: Seq[ModuleID] = Seq(
  "org.scalameta" %% "scalameta" % scalametaVersion
)

//todo: make this the scala version
val reflectionDependencies: Seq[ModuleID] = Seq(
  "org.scala-lang" % "scala-reflect" % "2.12.4"
)

val shapelessDependencies: Seq[ModuleID] = Seq(
  "com.chuusai" %% "shapeless" % shapelessVersion
)

val testDependencies: Seq[ModuleID] = Seq(
  "org.pegdown" % "pegdown" % pegdownVersion % "test",
  "org.scalacheck" %% "scalacheck" % scalacheckVersion % "test",
  "org.typelevel" % "shapeless-scalacheck_2.12" % shapelessScalacheckVersion % "test",
  "com.github.alexarchambault" %% "scalacheck-shapeless_1.13" % "1.1.6",
  "org.scalatest" %% "scalatest" % scalatestVersion
)

val catsDependencies: Seq[ModuleID] = Seq(
  "org.typelevel" % "cats-macros_2.12" % catsVersion,
  "org.typelevel" % "cats-core_2.12" % catsVersion,
  "org.typelevel" % "cats-kernel_2.12" % catsVersion
)



