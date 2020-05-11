Final and non-final errors, failed client validation and server bugs, HTTP status codes, and other things to consider in a client-server communication
=========================================================================================================================================================

Here are some general thoughts on two topics. First one covers a scenario when you're a client dealing with some third party API.
The second one is when you're someone's third party API, and you need to use a consistent strategy for error codes that your service can return.

As a client
^^^^^^^^^^^^^
There is a huge amount of errors that a third-party service you're intergrated with can return. Typically, they fall under a very
limited amount of types. Each type corresponds to some strategy to implemement. Besides, putting yourself in the shoes of a client
makes a perfect sense to come up with a consistent strategy regarding error response codes.

As a client, I need to consider the following scenarios:
 1. Can I even parse a response? That is, do I understand it?
 2. Is response successful, or not?
 3. Is it final?

What do I need to do in each of those cases?

Typically, it depends on whether a request you're performing is with side-effects or not. For example, if you're sending a request
for a bank card authorization, it apparently implies a side-effect: a certain sum of money should be withdrawn from someone's account.
Otherwise, if you want to get a list of a customer's previous orders, there is no side-effect involved.

Request with side effects
--------------------------
If you don't understand a response or can't obtain it whatsoever, you need to perform a manual handling.
The problem here is that you simply don't have an idea of what happened with a request. Was it successful? Or it wasn't? You don't know.
At the very least, you should log this response with a high severity.
Better option is to programmatically pass this problem to tech support, so that they can connect with a corresponding third party service's IT support.

If you can parse a response, and you see that it's not successful, it would be great to know whether you can try again. That is,
whether a problem is final or not. If it's not, you could save this request and try send it some time later.
This option improves overall user experience, so is highly preferred. If it's final, you can inform a customer right away,
so she can try something else.

If everything was fine and response was successful, it can be final and non-final, again. Typically this happens when you
send some withdrawal request, but due to the asynchronous nature of a fintech world, you can't know whether money were withdrawn right away.
Often times, you get a response like "Thanks, we got your request. We'll either send you a notification with a result, or you can request us for payment status from time to time".

Request without side effects
------------------------------
This one is way easier. If you can't parse a response, you can either try again, or you can tell your customer to try again.
For example, the easiest option is to reload a page.

If a response was not successful, or was not final, again, you don't need any complex manipulations. Just try again, or let your customer to do it.

As a server
^^^^^^^^^^^^
Since you already have an idea what a client has to deal with when integrating with a third-party service, you as a server can make her life easier.
In total, you have four states that you should handle explicitly:

 1. Invalid response (that is, the one that a client doesn't understand).
 2. Successful response.
 3. Final non-successful response.
 4. Non-final non-successful response.

Easy parts
-----------
OK, you can't handle a case when your response is not a part of your :doc:`schema <faq/how_to_parse_and_validate_json_schema_in_java>`. Because if you could, there would be no such responses whatsoever.

If a client request was successful, then it's easy: you just return http code 200.

What is non-successful response?
----------------------------------
The final two are about non-successful responses. First, you should have a firm grasp of what it is to be non-successful response.
Is it that a payment request was not processed right away? I doubt this one is an erroneous case. Non-successful responses
are for cases when there is no chance to be otherwise in principle. That is, when something's wrong with a customer's request data,
or with your service.

Final non-successful responses are for client errors
-----------------------------------------------------
So, third, final non-successful responses. If you give all those responses a long hard look, there is one thing in common to almost all of them:
they are such because of a customer's request data. It's the case in a vast majority of cases.
Should there was a different card number, or a different delivery address, or a different email, a request would a have a chance to be successful.
So, all the final non-successful responses should have an http status code 40X, a variation of a client error.

Non-final non-successful responses are for server errors
-----------------------------------------------------------
And, what does a non-final non-successful response look like? It's when your server behaves in an exceptional way. Typically, when there is a bug in your code,
or some other services you request return an error. Customer's request is perfectly fine, and she can try again.
Probably it was a network error somewhere in between you and, say, your database instance. Or it's a bug in your code that you might fix in a matter of minutes.
Anyways, an http error status 500 is an indicator that an error is transient.

Employ safety nets for code with significant side-effect
-----------------------------------------------------------
The problem here is that you should deliniate parts of code where a bug happened before any significant side-effects could have happened,
and where a bug happened after significant side-effects happened. For example, if an exception was thrown during validation, it's not so problematic.

But when an unexpected error occured after you have sent a payment request in a bank, you should treat this case extremely carefully.
This danger zone should be wrapped in a :code:`try ... catch` blocks, and if something went wrong, you should persist this state in a database.
Why? Because if a customer sends this request for the second time (you can't prohibit this), you don't want her to be charged twice.

Send an operation id
------------------------
Flash news: http is a stateless protocol. It can't know whether two requests are in any way similar. It is you who should take care of it.
How? Probably the easiest way is to make a customer to send some kind of an operation id, be it an order id generated by a frontend, or a transaction id, or something similar.
If she tries again, or reloads the page, the request will be sent with the same operation id, and we as a backend service, will process it accordingly.

I won't consider a request without side effects here, since it's way easier. Basically, it boils down to the fact that,
most of the time, no special treatment is needed.

--------------------------------------------------------------------------------------------------------------

A good way to implement complex validation in a :doc:`declarative <inspired_by/declarative_validation>`,
hence maintainable, way is to use the validol library.
It encourages :doc:`contextual validation <inspired_by/context_specific_validation>` and is infinitely extendable.
:doc:`Check it out <quick_start>`.