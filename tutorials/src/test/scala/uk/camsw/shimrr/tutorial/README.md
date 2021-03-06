# SHIMRR - The type-safe read-repair library tutorial

While working on the SHIMRR library I became nervous of building too much library code without trying it against real scenarios.
With no other project currently getting my attention I came up with... well, this.

Clearly the scenarios contained herein are somewhat tongue-in-cheek, but creating them forced me to think carefully
about potential use cases for the library and the overall shape of the API

Somewhere along that path I realised that creating these scenarios in 'tutorial form' might solve two problems in one go.  
So, due to my laziness, a tutorial for SHIMRR has become available sooner than perhaps it would have otherwise

    NOTE The early stages of the tutorial expose you to some of the 
    inner workings of SHIMRR.  You may wonder why your examples don't look as terse
    or declarative as those on the README that tempted you here in the first place.
    You may be concerned that you're being exposed to shapeless and defining implicits.
            
    Don't worry, once the concepts have been demonstrated in raw form we will introduce
    the DSL that makes it all easy.  This approach is deliberate. 'Magic' is less scary
    if you're allowed to peek inside the cauldron first!  If you want a view of how your
    code might look before investing more time reading, skip to...
    
[Use case 9 - Using the DSL](usecase9/README.md)    

## The structure

Each of the use-cases under this directory is self contained, can be built and contains passing tests

The tutorial takes you through an evolution of a simple component where offline data-migration is not acceptable
and a read-repair style approach has been adopted

It's recommended you read the README.md in each directory to get an idea of the use case being demonstrated.

The code itself is commented to identify the nuances of the new features introduced in the use case.  
It's *strongly* recommended that you play around with these scenarios, changing things to break the compilation

## Working with SHIMRR

Getting the library to this point has been hard - tracking down missing implicits is hard.  I hope that using it
proves easier.  More than usual I'd recommend making small changes.  It's really easy to guess what you've missed
when you are only defaulting one new field, much harder if you are trying to do twenty at the same time!

I hope the API makes life easy enough, feedback around this would be gratefully received as issues on *github*
 
# The use cases

1. [Creating the Product Catalogue component](usecase1/README.md)
2. [Defaulting a new field](usecase2/README.md)
3. [Removing the new field](usecase3/README.md)
4. [Defaulting a new field using SHIMRR](usecase4/README.md)
5. [Defaulting a field lazily](usecase5/README.md)    
6. [Scoped field defaulting](usecase6/README.md)
7. [Field type mapping & a need to refactor](usecase7/README.md)
8. [Rule composition](usecase8/README.md)
9. [Using the DSL](usecase9/README.md)
  