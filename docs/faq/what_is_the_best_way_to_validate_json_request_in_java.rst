What is the best way to validate json request in Java?
-------------------------------------------------------------
Problem
^^^^^^^^
Validating json request can be a tedious task. Its schema might be deeply nested, and you want to make sure not to have forgotten
all those json key existence checks. Validation constraints can include business-specific checks, not only related to format.
And they are usually way more complicated.
You want to keep them all in a specific place, the one everybody is well aware of.

All in all, if you take a look at your validation code couple of months later after it was initially written, it can look really scary.

How not to do it
^^^^^^^^^^^^^^^^^^
Very often, validation checks are tied to data model: every request field is explicitly mapped to data-storage column
and validation rules are put in a single place, which is those columns. Looks like a bliss, huh? It works well for simple
domain models, where all requests fall under the CRUD category. Have a user? Great, there is a domain class ``User``, corresponding
to ``user`` table. CRUD requests have the same fields a ``User`` class has.

Things start to look different in more complicated domains. What if a user can be "created" within two different scenarios?
The first one is when the user registers herself. Apparently, you don't want to lose her, so the validation rules are quite loose.
I believe you should give her a chance to fill non-critical info some time later.

The second one is when the user is registered by any kind of online-support staff. There is no second chance here, so validation
must be way more strict.

It's inherently tricky to put this kind of :doc:`contextual validation <../inspired_by/context_specific_validation>` in a data model,
because you have to have a way to distinguish between the two scenarios on data-model level, when it's already too late.

Solution
^^^^^^^^^
There is an alternative though. With `Validol <https://github.com/wrong-about-everything/Validol>`_ library,
your validation logic mirrors json structure. Complex validation checks decorate more basic ones. Besides, an entire validation logic
represents a single `expression <https://blog.kotlin-academy.com/kotlin-programmer-dictionary-statement-vs-expression-e6743ba1aaa0>`_.
And it encourages you to put validation in a specific scenario -- the one being currently validated.

Example
^^^^^^^^^
Consider a schema to be validated:

.. code-block:: JSON

    {
       "where":{
          "building":1,
          "street":"Red Square"
       }
    }

Validation logic reflects the structure of json schema. All the constraints are described right in the structure itself.
All the mundane checks like json key existence are already taken care of.

Here it goes:

.. code-block:: java
    :linenos:

    new FastFail<>(
        new IsJsonObject(
            new WellFormedJson(
                new IndexedValue("where", jsonString)
            )
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
        .result();

There is no familiar spaghetti-code here. Only pure gluten-free :doc:`declarative expressions <../inspired_by/declarative_validation>`.
Let's take a look what's going on here, line by line.

| ``Line 1``: The whole validation is a fast fail thing, the one returning an error if the first argument results in an error.
| ``Line 4``: THe first argument is a declaration of a ``where`` block.
| ``Line 3``: It must be a well-formed json.
| ``Line 2``: Besides, it should be a json object.
| ``Line 7``: The second argument is a closure. It's first argument is a ``where`` json object.
| ``Line 8``: Here goes the named block of named elements.
| ``Line 9``: Its name is ``where``.
| ``Line 10``: The second argument is a list of all elements.
| ``Line 13``: The first element is ``street``.
| ``Line 12``: It's required.
| ``Line 11``: And should be represented as string.
| ``Line 18``: The second one is ``building``.
| ``Line 17``: It's required as well.
| ``Line 16``: And should be represented as an integer.
| ``Line 22``: If all previous checks are successful, an ``Where`` object is created.
| It's first argument is `street`, which must be a String; the second one is `building`, which must be an integer.


Also, take a look at :doc:`quick-start section <../quick_start>` for more examples and line-by-line code analysis.
