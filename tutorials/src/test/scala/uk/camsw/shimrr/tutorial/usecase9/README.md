# Use case 9 - Using the DSL

So far you've been happy that you can define the migrations that you want,
but less happy having to declare implicits, remembering to import scopes
etc.  There seems to be quite a lot of boiler-plate, amongst which the
actual rules get a little lost.

The README that tempted you to look at this library showed a reasonably
declarative DSL, so you try to use it.  As an aside, you've also been
asked to rename price to retailPrice

## Use Case

As a conscientious developer
- I want to clean up the code    
- because it is a bit fiddly  
- and I want it to be more declarative

As a developer on the web app
- I want the price field to be renamed to retailPrice
- because that's what it is
- and we are getting it confused with cost price which we get from somewhere else


## You decide that 

- A field rename might be as simple as a parameterized defaulter
- You'll try to define the pipeline using the DSL 

## Summary

At the end of this scenario you will have

 - Used the pipeline DSL
 - Renamed a field
 - Had a fairly comprehensive overview of everything the library currently has to offer 
 
 
[The code]()

That covers the basics and, I think, the majority of use cases.  For more information, 
examples, patterns and testing - refer to [the cookbook](../cookbook/README.md)