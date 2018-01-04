# Use Case 1 - Creating the *Product Catalogue* component

Your start-up company has decided to focus on selling bicycles online.
  
For some reason (known only to management) they have decided to build their own system
rather than take one *off the shelf*

Someone down the pub mentioned that micro-services solve all problems both now and forever, 
thus you have been given the task of creating a service that allows other components to query the *Product Catalogue*.
Another team (that does not exist yet) will be dealing with the Create/ Update and Delete functionality

Of course, *big data* techniques must be applied everywhere at all times, thus your CTO has opted 
for *no-sql* storage solution


## Use Case

As a potential customer 
- I want to see all available bicycles 
- in order that I can decide which one to buy
- thus generating revenue for my company

## Getting Started

Because you listened during the mandatory *We Are Agile* presentation given by your *agile coach* during your induction
you decide to begin by creating a simple service, with hard-coded product definitions stored in-memory

When you speak to *business* (also the CTO) you discover that:
- You only sell bicycles
- You only need to support
    - make
    - model
    - price (which you decide to model as a float because nobody told you about rounding errors during induction)
    
## You begin creating

- The model
- A service, providing access to all products
- An in-memory store (which you decide to abstract at this stage as you know something *bigger* is coming)

## Summary

At the end of this scenario you will have
 - Been introduced to the skeleton of the (somewhat tongue-in-cheek) solution we are trying to evolve
 during this tutorial 
 
[The code]()

[Next use case](../usecase2/README.md)

 



    


