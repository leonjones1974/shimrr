# Use case 4 - Defaulting a new field using SHIMRR

In the build up to the religious festival of your choice the CTO has decided to offer 
a 10% discount on all products.  Time is tight so rather than spinning up yet another 
micro-service you have been asked to add a discount percentage field to each of the products.

You question whether this really belongs on the model but the CTO assures you that upcoming
stories under the (somewhat grandiosely named) *Marketing Campaigns* epic will warrant it. 

## Use Case

As a potential customer 
- I want to see a 10% discount for all products we offer 
- as it's nearly <festival> and we think it will boost sales 
- thus increasing turnover for the company


## You decide that you need to

- Add yet another new version of the Bicycle with the discount field defaulted to 10% for everything
- Create all migrations
- Continue using shimrr as the documentation claims it can help and it worked last time 

## Summary

At the end of this scenario you will have

 - Created a global rule to default a missing field to a given value
 
[Next use case](../usecase5/README.md)

 



    


