Declarative validation
------------------------------------
What is declarative?
^^^^^^^^^^^^^^^^^^^^

`Declarative code <https://en.wikipedia.org/wiki/Declarative_programming>`_ describes, or declares, what the desired result should look like. At the same time, it doesn't explicitly
specify the steps needed to reach its goal.
Wikipedia has a bit more formal definition, yet still readable:
Declarative programming is a programming paradigm — a style of building the structure and elements of computer programs — that expresses the logic of a computation without describing its control flow.

As we see, declarative programming has nothing to do with the language. You may very well use dense imperative code in Haskell. You can write declarative code in PHP.

It does have something to do with you though.

Quick example of declarative and imperative code, deliberately in pseudo-code. Unit here is a main building block in your language of choice.
Typically, it's either a class or a function.

Here is a declarative one:

.. code-block:: java

    unit sum {
        (int a, int b) => a + b
    }

Why is it declarative in the first place? Because only a desired result is described, which is a sum. You can implement it by a number of ways,
not only by addition.


Nevertheless, when one needs a sum, the following code is typical:

.. code-block:: java

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
Second, you don't have to send an http request and parse its response. Instead, you need either a
`command <https://www.enterpriseintegrationpatterns.com/patterns/messaging/CommandMessage.html>`_ result
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
Nope. Declarative code is much more than mechanical actions. Simply sticking your `service classes <https://www.yegor256.com/2014/05/05/oop-alternative-to-utility-classes.html>`_
in a config file won't bring you closer to maintainable code.

Using functions does not ensure your code to be declarative either. You can wrap each implementation step into its own function
(or a service class), as in a previous controller example. But it still remains susceptible to specific kinds of changes.

Declarative validation
^^^^^^^^^^^^^^^^^^^^^^^
If you're tired of spaghetti validation code mess, you can try a `declarative approach <https://github.com/wrong-about-everything/Validol>`_.
Class names reflect what is validated, not how. Their implementation doesn't clutter the higher-level validation logic description.
It's especially convenient in case of complex json data structures, when your validation composite object reflects the request structure.
And as a nice declarative-approach side-effect, your code is not `temporally coupled <https://blog.ploeh.dk/2011/05/24/DesignSmellTemporalCoupling/>`_.

To get a feel of what it looks like, consider a following example of a complex request validation.
JSON structure looks the following:

.. code-block:: JSON

    {
       "guest":{
          "phone":"+44123456789",
          "name":"Vasily Belov",
          "email":"vasya@belov.com"
       },
       "bag":{
          "items":[
             {
                "id":888
             },
             {
                "id":777
             }
          ],
          "discount":{
             "promo_code":"VASYA1988"
          },
          "served_for":3
       },
       "delivery":{
          "type_id":20,
          "where":{
             "restaurant_id":1
          },
          "when":{
             "datetime":"2019-10-23T08:33:11.798400+00:00"
          },
          "from_where":{
             "restaurant_id":1
          }
       },
       "payment":{
          "type_id":30
       },
       "source":20
    }

The semantics basically doesn't really matter, though you can guess that it has something to do with food delivery order registration.
Schema is quite large. Typically, validation code is less then clear.
Here is the declarative validation composite with `Validol library <https://https://github.com/wrong-about-everything/Validol/>`_
(check :doc:`Validol's Quick start page <../quick_start>` for a line-by-line analysis, it's not as scary as you imagine):

.. code-block:: java
    :linenos:

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
                                        // Here goes the condition whether this order should be delivered by courier or picked up.
                                        // It's omitted for brevity.
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

What catches the eye first? There are plenty of ``FastFail`` gizmos. This class accepts exactly two arguments:
original element and closure. Whether the first parameter results in true, the second closure is invoked.
The typical cases are just as in an example:

 - Check whether the request represents a well-formed json
 - Check whether some key is present (like ``new IndexedValue("when", deliveryJsonElement)``)

Second, the object structure reflects the request structure. It might seem to (and actually could)  be a drawback,
since the request can be extremely complex. The solution is pretty simple: you can represent each semantic block as its own class,
like the following:

.. code-block:: java

    new FastFail<>(
        new WellFormedJson(
            new Unnamed<>(Either.right(new Present<>(this.jsonRequestString)))
        ),
        requestJsonObject ->
            new UnnamedBlocOfNameds<>(
                List.of(
                    new Guest(requestJsonObject),
                    new Items(requestJsonObject),
                    new RequiredNamedBlocOfCallback<>(
                        "delivery",
                        requestJsonObject,
                        deliveryJsonElement ->
                            new SwitchTrue<>(
                                "delivery",
                                List.of(
                                    new Specific<>(
                                        // pretty dumb clause
                                        () -> true,
                                        new Courier(deliveryJsonElement)
                                    )
                                )
                            )
                    ),
                    new Source(requestJsonObject)
                ),
                OrderRegistrationRequestData.class
            )
    )

If you have a block structure that depends on passed type (like ``type_id``), ``SwitchTrue`` is your friend. It represents a sort of declarative
switch-case expression, where the value checked against is always ``true`` (hence the name, ``SwitchTrue``).

If everything's successful, you get a data object reflecting the request structure, with type hinted values:

.. code-block:: java

    Result<OrderRegistrationRequestData> result = new ValidatedOrderRegistrationRequest(jsonRequest).result();
    // get an email:
    result.value().raw().guest().email();

In case of error:

.. code-block:: java

    assertFalse(result.isSuccessful());
    // Error is represented as generally as Object itself,
    // for it can be a String, a List<String>, a Map<String, String>, a Map<String, Map<String, String>>, or mixed.
    result.error();

There is plenty of space left to extend the logic, just add another validating decorator.

More examples
^^^^^^^^^^^^^^
Check out more usage examples `here <https://github.com/wrong-about-everything/Validol/tree/master/src/test/java/example>`_.
`Here <https://github.com/wrong-about-everything/Validol/tree/master/src/test/java/example/correct/inline>`_ is an example of inline validation in greater detail.
`Here <https://github.com/wrong-about-everything/Validol/tree/master/src/test/java/example/correct/split>`_ you can find out how to split the validation logic according to semantic request blocks.
