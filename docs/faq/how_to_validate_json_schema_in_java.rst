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

There is no usual spaghetti-code code here. Only pure :doc:`declarative expressions <../inspired_by/declarative_validation>`.

Also, take a look at :doc:`quick-start section <../quick_start>` and check my post on
:doc:`contextual validation <../inspired_by/context_specific_validation>` with more examples and line-by-line analysis.
