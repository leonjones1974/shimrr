# Scenario 2 - More data

Your service is now in production using real product data stored in a no-sql backend

The CTO has been monitoring *user journeys* (using a tool that someone from the golf club told him about)
and has noticed lots of people dropping off.  Some of these potential customers have been emailed, asking
about product *lead times*

Wearing his *stakeholder hat* the CTO presents you with the following (urgent) use case 

## Use Case

As a potential customer 
- I want to see the expected lead time (in days) when I am browsing products 
- in order that I can purchase immediately without contacting the Bicycles Only Ltd contact-centre 
- thus generating even more revenue for my company

## Technical Constraints

As a big data solution (you sell literally 100's of different bicycles) that is in production you
are told in no uncertain terms that migrating the existing bicycles in the repository is not an option
At a recent conference held in Shoreditch your CTO heard a clever person talking about *read-repair*
He/ She (take your pick) explains that in this model, old data is *repaired* at read time as required
and thinks this approach should be applied here 

The CTO advises you that existing products should be assumed to have a lead-time of 7 days because
sometimes that will be true

## You decide that you need to

- Version the model because you really like the fact your service is type-safe and you don't like to 
dilute that by having sparsely populated models full of Options.  Anyway, in this case an option would
be misleading because you've been given a default

You set about updating everything

 



    


