# Use case 6 - Scoped field defaulting

The warehouse is rapidly filling up with penny farthings because although it seemed like
a quirky *eye-catching* idea initially it has quickly become apparent that penny farthings went 
out of fashion for a good reason - they are incredibly uncomfortable and few members of the
public would want to be seen riding one... even if they could!

The *offers team* team have enhanced their service to provide the default discount based on 
make and model of the bicycle and have asked you to use that API when defaulting discounts.

Anything with a persisted discount should be left *as-is* 
   

## Use Case

As someone in the promotions team 
- I want to default the discount percentage based on the make and model of the bicycle  
- in order that I can make space in the warehouse for the 1000 Sinclair C5's that are on order 
- thus replacing stock that nobody wanted with...


## You decide that 

- Again, there is no need to introduce a new version as the structure of a Bicycle hasn't changed
- Somehow you need access to the make and model at the point of defaulting the discount percentage

## Summary

At the end of this scenario you will have

 - Been introduced to *scoped* contexts (at the abstract, coproduct level)
 - Defined rules that use data in the current version while defaulting new fields
 
[The code]()

[Next use case](../usecase7/README.md)