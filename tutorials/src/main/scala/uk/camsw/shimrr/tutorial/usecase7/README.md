# Use case 7 - Field type mapping & a need to refactor

The newly recruited finance offer has spent the last 3 weeks tring to find the 4.5p discrepancy 
in the figures.  Since the halfpenny has been out of circulation since 1984 the conclusion
is that something, somewhere has a rounding error.  

The CTO nods wisely and points you away from float and toward BigDecimal 
   

## Use Case

As the finance office 
- I want prices to be held in such a way that they do not suffer from rounding errors  
- because exactness is important to me 
- and helps me sleep at night


## You decide that 

- This time you definitely do need a new version of bicycle
- Due to price being on all versions you can pull it up to Bicycle. It helped last time after all
- It feels like you need a combination of
   - Removing the old field
   - Defaulting the new field (using the value of the old field that you've just removed)
- It is not going to be simple

## Once you begin

You realise that pulling price up to Bicycle is not going to work as it will then
be a float on the trait and we want it to be a BigDecimal in the new version
You contemplate:
- Adding a type parameter to Bicycle, i.e Bicycle[BigDecimal] 
  - it feels too complex, will surely become crazy in the future and besides, you 
  don't like libraries that force you to work around them!
- Having two fields, one for the old and one for the new
  - it confuses you, let alone the consumers of Bicycle
- In the end you decide to scope the rules at the concrete level       

## Summary

At the end of this scenario you will have

 - Scoped rules at the concrete (product) level
 - Become extremely unhappy with the result of your efforts.  You have been forced
   to duplicate all rules in order to achieve one specialisation 
 - Got a better understanding that a field is a combination of both its identifier
 and its type in SHIMRR
 
# Follow up

Having got a better understanding of how basic scoping works you decide to take a step
back and determine whether there's a refactor that will make the rules more representative
of what they are trying to express  

 
[The code]()

[Next use case](../usecase8/README.md)