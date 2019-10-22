.. toctree::

It's declarative
------------------------------------
What is declarative?
^^^^^^^^^^^^^^^^^^^^

https://medium.com/front-end-weekly/imperative-versus-declarative-code-whats-the-difference-adc7dd6c8380


Declarative code describes, or declares, what the desired result should look like. At the same time, it doesn't explicitly
specify the steps needed to reach its goal.
Wikipedia has a bit more formal definition, yet still readable:
Declarative programming is a programming paradigm — a style of building the structure and elements of computer programs — that expresses the logic of a computation without describing its control flow.

As we see, declarative programming has nothing to do with the language. You can use dense imperative code in Haskell. You can write declarative code in PHP.

It does have something to do with you though.

Quick example of declarative and imperative code, deliberately in pseudo-code. Unit here is a main building block in your language of choice.
Typically, it's either a class or a function.

Here is a declarative one:

unit sum {
    (int a, int b) => a + b
}

Why is it declarative in the first place? Because only a desired result is described, which is a sum. You can implement it by a number of ways,
not only by addition.


Nevertheless, when one needs a sum, the following code is typical:

unit add {
    (int a, int b) => a + b
}

What a big deal, just a different name, you might say. But naming is what reflects your way of thinking.
Hence, there is a small difference in this situation, where we have only two basic programming units. And it's huge
when we have 1 million lines of code.

The consequences of being imperative on a bigger scale
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
How do your user stories (or Application services, or Controllers) look like? I bet you recognize the following pattern:

1. Validate a request
2. Send some http request
3. Parse response
4. Update some entity
5. Save it in a database

Quick declarativeness test: you API changes, 3rd party API changes either, including the number and format of requests,
thus parsing logic changes, consequently entity update logic does either, and finally, however crazy, you DB vendor changes.
Does you controller code have to change? If yes -- bad news, sorry: your code is probably imperative. I hope you have controller tests in place.
But chances are you don't. Anyways, you're facing a changes that affect a significant area of your codebase.

Declarative approach helps you mitigate those issues. First, you don't have to validate the request. You just need a *validated* request.
Second, you don't have to send an http request and parse its response. Instead, you need either a command result [https://www.enterpriseintegrationpatterns.com/patterns/messaging/CommandMessage.html]
(like whether the transaction authorization was successful or not) or particular data (like special marketing offer for current client).
And finally you don't have to update an entity and save it in database; instead you want it persisted.

So as a result we get the following list of expressions:
1. Validated request
2. Information about either successfulness or remote entity data
3. Persisted local object as a result

There are no implementation details left on this user-story level. Thus, your design will be subjected to less changes
in case of any requirement changes. The scope of changes will be smaller, which means more readable and maintainable code.

Declarative code probably can be considered as a special case of encapsulation.
But there are plenty of ways to organize your classes in such a way that encapsulation is respected. Declarative code is
both encapsulated and descriptive, contrary to prescriptive.

What declarative is not?
^^^^^^^^^^^^^^^^^^^^^^^^
One of the miconceptions I see quite often is that the fact that you've put all your dependencies in a config file automatically makes your code declarative.
Nope. Declarative code is much more than mechanical actions. Simply sticking your service classes [https://www.yegor256.com/2014/05/05/oop-alternative-to-utility-classes.html]
in a config file won't bring you closer to maintainable code.

Declarative validation
^^^^^^^^^^^^^^^^^^^^^^^
Class names reflect what is validated, not how. Their implementation doesn't clutter the higher-level validation logic description.
To get a feel of what it looks like, here is a quick example:

.. code-block:: java
    :linenos:

::
    new FastFail<>(
        new WellFormedJson(
            new Unnamed<>(Either.right(new Present<>(this.jsonRequestString)))
        ),
        requestJsonObject ->
            new UnnamedBlocOfNameds<>(
                List.of(
                    new FastFail<>(
                        new IsJsonObject(
                            new Required(
                                new IndexedValue("guest", requestJsonObject)
                            )
                        ),
                        guestJsonObject ->
                            new NamedBlocOfNameds<>(
                                "guest",
                                List.of(
                                    new AsString(
                                        new Required(
                                            new IndexedValue("email", guestJsonObject)
                                        )
                                    ),
                                    new AsString(
                                        new Required(
                                            new IndexedValue("name", guestJsonObject)
                                        )
                                    )
                                ),
                                Guest.class
                            )
                    ),
                    new FastFail<>(
                        new Required(
                            new IndexedValue("items", requestJsonObject)
                        ),
                        itemsJsonElement ->
                            new NamedBlocOfUnnameds<>(
                                "items",
                                itemsJsonElement,
                                item ->
                                    new UnnamedBlocOfNameds<>(
                                        List.of(
                                            new AsInteger(
                                                new Required(
                                                    new IndexedValue("id", item)
                                                )
                                            )
                                        ),
                                        Item.class
                                    ),
                                Items.class
                            )
                    ),
                    new FastFail<>(
                        new Required(
                            new IndexedValue("delivery", requestJsonObject)
                        ),
                        deliveryJsonElement ->
                            new SwitchTrue<>(
                                "delivery",
                                List.of(
                                    new Specific<>(
                                        () -> true,
                                        new UnnamedBlocOfNameds<>(
                                            List.of(
                                                new FastFail<>(
                                                    new IndexedValue("where", deliveryJsonElement),
                                                    whereJsonElement ->
                                                        new NamedBlocOfNameds<>(
                                                            "where",
                                                            List.of(
                                                                new AsString(
                                                                    new Required(
                                                                        new IndexedValue("street", whereJsonElement)
                                                                    )
                                                                ),
                                                                new AsInteger(
                                                                    new Required(
                                                                        new IndexedValue("building", whereJsonElement)
                                                                    )
                                                                )
                                                            ),
                                                            Where.class
                                                        )
                                                ),
                                                new FastFail<>(
                                                    new IndexedValue("when", deliveryJsonElement),
                                                    whenJsonElement ->
                                                        new NamedBlocOfNameds<>(
                                                            "when",
                                                            List.of(
                                                                new AsDate(
                                                                    new AsString(
                                                                        new Required(
                                                                            new IndexedValue("date", whenJsonElement)
                                                                        )
                                                                    ),
                                                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                                )
                                                            ),
                                                            DefaultWhen.class
                                                        )
                                                )
                                            ),
                                            CourierDelivery.class
                                        )
                                    )
                                )
                            )
                    ),
                    new AsInteger(
                        new Required(
                            new IndexedValue("source", requestJsonObject)
                        )
                    )
                ),
                OrderRegistrationRequestData.class
            )
    )
        .result()

It's especially convenient in case of complex json data structures.
Your validation composite object reflects the request structure.
And as a nice declarative-approach side-effect, your code is not temporally coupled [https://blog.ploeh.dk/2011/05/24/DesignSmellTemporalCoupling/].

