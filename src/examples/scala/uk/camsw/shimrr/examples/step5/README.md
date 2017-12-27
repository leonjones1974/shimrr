# Scenario 5 - Lazy field initialization

As the organization fleshes out it's offer solution the CTO makes it clear that he wants to be 
able to change the global default discount percentage without redeploying your component

The offers and discounts team have built a simple component that exposes the current default discount
and have given you a stub to code against

## Use Case

As someone in the promotions team 
- I want to be able to change the default discount percentage quickly  
- in order that I can respond to the market without a dependency on technology 
- thus increasing sales revenue due to improved agility


## You decide that 

- This time you don't need to introduce any new bicycle versions
- But you do need to remove the hard-coded literal 10% discount and replace it with a service call
- For the purposes of this tutorial you are not going to bother implementing the error handling that you 
would normally prioritise in an architecture of this nature!

## Summary

At the end of this scenario you will have

 - Created a global rule to default a missing field lazily, calling out to a service to obtain the value