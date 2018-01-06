# SHIMRR
### A *type-safe* read-repair library (for scala)

## At a glance...
```scala

   
  sealed trait Version
  case class NoFields() extends Version
  case class Str1(stringField1: String) extends Version
  case class Str1Str2(stringField1: String, stringField2: String) extends Version
  case class Str1Str2Int1(stringField1: String, stringField2: String, intField1: Int) extends Version 
  

  "Given a valid pipeline" - {
 
     @pipeline
     val pipeline = new PipelineDsl4[NoFields, Str1, Str1Str2, Str1Str2Int1] {
 
       from[NoFields] {
         'stringField1 -> "str1"
       }
 
       from[Str1] {
         'stringField2 -> (() => "str2")
       }
 
       from[Str1Str2] {
         'intField1 -> ((_: Str1Str2) => 25)
       }
 
       from[Str1Str2Int1] {}
 
     }
 
     import pipeline.exports._
     import syntax._
      
     "all migrations should default correctly" in {
       GeneratorDrivenPropertyChecks.forAll((in: Version) => {
 
         val out = in.migrateTo[Str1Str2Int1]
         in match {
           case _: NoFields => out shouldBe Str1Str2Int1("str1", "str2", 25)
           case x: Str1 => out shouldBe Str1Str2Int1(x.stringField1, "str2", 25)
           case x: Str1Str2 => out shouldBe Str1Str2Int1(x.stringField1, x.stringField2, 25)
           case x: Str1Str2Int1 => out shouldBe Str1Str2Int1(x.stringField1, x.stringField2, x.intField1)
         }
       })
     }
   }
   

```

## Overview

 - Migrate/ Upgrade Coproducts/ Products
 - Compile time failure for any incomplete migrations
 - Automatically drop remove fields
 - Automatically re-order fields
 - Default new fields using a simple, type-safe rules dsl
 - Migrate collections of CoProducts/ Products (or anything for which a cats.Functor can be summoned)
 
## Why was this library created

- Maintaining a stack of read-repair *shims* is hard work and becomes increasingly complex over time
- The version mapping code is largely boiler-plate and is dull to work with
 
### Disclaimer

The inspiration for this project (along with a reasonable amount of the initial code) was taken from the excellent book

[The type astronaut's guide to shapeless](https://underscore.io/books/shapeless-guide/)

## Getting started

1. Add the shimrr core and shimrr macros projects as dependencies
2. Include the macro paradise compiler plugin
```scala

val shimrrDependencies: Seq[ModuleID] =Seq(
  "uk.camsw" %% "shimrr-core" % "1.0.0",
  "uk.camsw" %% "shimrr-macros" % "1.0.0"
)


lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M10" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  scalacOptions in(Compile, console) := Seq() 
)

lazy val myProject = project
  .settings(metaMacroSettings)
  .settings(libraryDependencies ++= shimrrDependencies)
  
```


## The tutorial

The tutorial is a WIP and is being used to drive future development of the library.  Visit it regularly
to find the latest enhancements

[The SHIMRR tutorial is here](tutorials/src/test/scala/uk/camsw/shimrr/tutorial/README.md)
 

## How to build and test
```bash
sbt clean update test
```

## The backlog
Is currently held in trello, but feel free to create issues on github as I'll migrate soon
