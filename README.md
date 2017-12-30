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

[The SHIMRR tutorial is here](tutorials/src/main/scala/uk/camsw/shimrr/tutorial/README.md)
 
## At a glance...
```scala

   "multiple migrations at the product level" in {
     @migration
     val str1Rules = new Dsl[Str1] {
     
         'stringField2 -> "str2"
         
         'intField1 -> 25
     }
    
     @migration
     val str1Str2Rules = new Dsl[Str1Str2] {
     
        'intField1 -> 51
       
     }
    
     import str1Rules.exports._
     import str1Str2Rules.exports._
    
     Str1("str1").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 25)
     Str1Str2("str1", "str2").migrateTo[Str1Str2Int1] shouldBe Str1Str2Int1("str1", "str2", 51)
   }
   

```

## How to build and test
```bash
sbt clean update test
```

## The backlog
Is currently held in trello, but feel free to create issues on github as I'll migrate soon
