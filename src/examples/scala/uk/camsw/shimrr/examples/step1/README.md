# Scenario 1 - The Product Catalogue

Your start-up company has decided to focus on selling bicycles online.
  
For some reason (known only to management) they have decided to build their own system
rather than take on *off the shelf*

Someone down the pub mentioned that micro-services solve all problems both now and forever, 
thus you have been given the task of creating a service that manages the *Product Catalogue*

Of course, *big data* techniques must be applied everywhere at all times, thus you have opted 
for *no-sql* style storage back-end


## Use Case

As a potential customer 
- I want to see all available bicycles 
- in order that I can decide which one to buy
- thus generating revenue for my company

## Getting Started

Because you listened during the mandatory *We are agile* speech given by your *agile coach* during your induction
you decide to start by creating a simple service, with hard-coded products definitions stored in-memory

When you speak to *business* (also the CTO) you discover that:
- You only sell bicycles
- You only need to support
    - make
    - model
    - price (which you decide to model as a float because nobody told you about rounding errors during induction)
    
### The bits to be created
- The model
- A service, providing findAll
- An in-memory store (which you decide to abstract at this stage as you know something *bigger* is coming)

 



    


