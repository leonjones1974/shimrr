# SHIMRR

## Overview

SHIMRR is a type-safe read-repair library providing the following functionality:

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

## The tutorial

The tutorial is a WIP and is being used to drive future development of the library.  Visit it regularly
to find the latest enhancements

[The SHIMRR tutorial is here](src/tutorial/scala/uk/camsw/shimrr/tutorial/README.md)
 
## At a glance...
```scala

    "scopes can be applied to lists containing instances of a given coproduct" in {
      import context.scoped._

      sealed trait Versioned
      case class V1() extends Versioned      
      case class V2(name: String) extends Versioned      

      implicit val v1Rules = MigrationContext[V1](
        'name ->> "Leon Jones" :: HNil
      )

      implicit val v2Rules = MigrationContext[V2]()
      
      val xs = List[Versioned](
        V1(),
        V2("Nicky Hayden")        
      )

      xs.migrateTo[V2] shouldBe List(
        V2("Leon Jones"),
        V2("Nicky Hayden")
      )
    }

```

## How to build and test
```bash
sbt clean update test
```

## The backlog
Is currently held in trello, but feel free to create issues on github as I'll migrate soon
