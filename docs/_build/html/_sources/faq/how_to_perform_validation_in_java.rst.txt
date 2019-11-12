How to perform validation in Java?
-------------------------------------------------------------

First, put it into a particular scenario -- the one you're validating
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
First of all, what is validation? For me, it's a way to check whether the current scenario can be carried out.
So this activity is inherently :doc:`contextual <../inspired_by/context_specific_validation>`,
that is, it's dependent on specific scenario which is currently validated.

Besides, it's a user-friendly way of reporting errors. If you put validation logic in domain, there is a problem of mapping error to request fields.
So I don't even bother with it. I put only fundamental checks in domain, like Price which is always positive. In case of violation,
I throw an exception without hesitation:

.. code-block:: java

    final public class Price
    {
        private Integer amount;

        public Price(Integer amount) throws Exception
        {
            if (amount < 0) {
                throw new Exception("Amount can not be empty");
            }

            this.amount = amount;
        }

        // some logic
    }

So it doesn't fit my definition of the word "validation". It's more of a `domain constraint`.

Quick example
++++++++++++++++++
Consider an food delivery order that can be registered by client online, and by an online support operator.
In the first case, there is a chance that guest will make a typo in a delivery address, and the backend system fails to find the nearest restaurant.
Is it a legal case to lose an order? I bet it's not. So it's perfectly fine to register an order even if it's not known which restaurant's gonna deliver it.
In this case, support staff finds a way to connect with a client (either by phone or an email, or, if nothing worked out, by finding his name in a client database).

Now, consider the second case: online support operator registers an order. Validation is clearly stronger. No typos are allowed, since there is no chance to fix them.
So if there is no way the backend system can find a restaurant, support staff should have the ability to set it manually somehow.

The moral of the story: don't put validation in any central place. It will hit you hard some day.

Do it declaratively
^^^^^^^^^^^^^^^^^^^^^^^^^^^
:doc:`Declarative code <../inspired_by/declarative_validation>` means effectively more readable, more maintainable code.
Generally, it's the code written with what you need as a result in mind, not how exactly you should achieve it.

As an example, consider the following request (actually, some part of it) represented with the following json schema:

.. code-block:: JSON

    {
       "when":{
          "datetime":"2019-11-29 20:08:12+01:00"
       }
    }

It's a request for order delivery. There are quite a few checks we need to check, starting with json correctness,
then the structure as a whole, the required fields, their format, and further some more intricate business constraints specific to the concrete scenario:

.. code-block:: java
    :linenos:

    new FastFail<>(
        new IsJsonObject(
            new WellFormedJson(
                new IndexedValue("when", jsonString)
            )
        ),
        whenJsonElement ->
            new NamedBlocOfNameds<>(
                "when",
                List.of(
                    new RestaurantIsAbleToDeliverBySpecifiedTime(
                        new IsGreaterThan(
                            new AsDate(
                                new AsString(
                                    new Required(
                                        new IndexedValue("datetime", whenJsonElement)
                                    )
                                )
                            ),
                            new Now().value()
                        ),
                        this.dbConnection
                    )
                ),
                When.class
            )
    )
        .result();

The whole validation logic constitutes in a single expression. The whole validation is a FastFail (declared on (``Line 1``)) block, the one returning an error
if the first argument results in an error. So the first thing is to check whether it's a valid json at all (``Line 3``), then whether it's a
json object (``Line 2``). If everything's fine, a closure is invoked (``Line 7``), with the first argument being the well-formed json structure.
Then the structure itself is validated: it's a block (``Line 8``) named ``when`` (``Line 9``) consisting of other named entities. In this particular case
we expect a single key called ``datetime`` (``Line 16``). It's required (``Line 15``), turned to string (``Line 14``) and checked whether it's a valid date (``Line 13``)
which is greater than now (``Line 12``). Finally, I involve a database to find out whether some restaurant is able to deliver
this order by specified time. If all checks pass, an object of class ``When`` is created.

This code is backed by `Validol <https://github.com/wrong-about-everything/Validol>`_ library. Check out more examples in :doc:`Quick start <../quick_start>` section.
