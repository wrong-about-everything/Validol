FAQ
=====

This is a list of frequently asked questions on validation.

What is the best way to validate json in Java?
-------------------------------------------------------------
With `Validol <https://github.com/wrong-about-everything/Validol>`_, you can do it like that:

.. code-block:: java

    public class ValidatedRequest implements Validatable<JsonElement>
    {
        private String jsonRequestString;

        public ValidatedRequest(String jsonRequestString)
        {
            this.jsonRequestString = jsonRequestString;
        }

        @Override
        public Result<JsonElement> result() throws Throwable
        {
            return
                new WellFormedJson(
                    new Unnamed<>(Either.right(new Present<>(this.jsonRequestString)))
                )
                    .result()
                ;
        }
    }

Chances are your request is more complicated. Your validation logic can be much more complex. I suggest that you tackle it
in a declarative fashion. For a line-by-line example take a look at :doc:`Quick-start <quick_start>` section.
Also consider having a glance at :doc:`contextual validation post <inspired_by/context_specific_validation>` and
my take on declarative programming in general and
:doc:`declarative validation <inspired_by/declarative_validation>` in particular, there you can find some more examples.

How to validate json schema in Java?
-------------------------------------------------------------
Validating json schema can be a tedious task. Besides, take a look at validation code couple of months later after it was initially written.
It can look really scary.

There is an alternative though. With `Validol <https://github.com/wrong-about-everything/Validol>`_ library,
your validation logic mirrors json structure. Complex validation checks decorate more basic ones. Besides, an entire validation logic
represents a single `expression <https://blog.kotlin-academy.com/kotlin-programmer-dictionary-statement-vs-expression-e6743ba1aaa0>`_.

Consider a schema to be validated:

.. code-block:: JSON

    {
       "where":{
          "building":1,
          "street":"Red Square"
       }
    }

Here is what validation logic looks like. It reflects the structure of json schema.
All the constraints are described right in the structure itself.
All the mundane checks like json key existence are already taken care of.

.. code-block:: java

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
                                new IndexedValue("delivery", requestJsonObject)
                            )
                        ),
                        deliveryJsonObject ->
                            new NamedBlocOfNameds<>(
                                "delivery",
                                List.of(
                                    new FastFail<>(
                                        new IsJsonObject(
                                            new IndexedValue("where", deliveryJsonObject)
                                        ),
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
                                    )
                                ),
                                CourierDelivery.class
                            )
                    )
                ),
                OrderRegistrationRequestData.class
            )
    )
        .result();

There is no usual spaghetti-code code here. Only pure declarative expressions.

Also, take a look at :doc:`quick-start section <quick_start>` and check my post on
:doc:`contextual validation <inspired_by/context_specific_validation>` with more examples and line-by-line analysis.


What is the best Java email address validation method?
-------------------------------------------------------------
c

How to perform validation in Java?
-------------------------------------------------------------
d

Where should I put a validation logic?
-------------------------------------------------------------
e

How to check whether a given string is valid JSON in Java?
-------------------------------------------------------------
f

How to check whether an URL is valid in Java?
-------------------------------------------------------------
g

How to validate IPv4 string in Java
-------------------------------------------------------------
h

Where should validation go in Domain-Driven Design
-------------------------------------------------------------
i

Should I throw an exception or return a bool value in validation?
---------------------------------------------------------------------
j

How to perform a validation against regex in Java?
-------------------------------------------------------------
k

How to reduce a cyclomatic complexity in validation?
-------------------------------------------------------------
l