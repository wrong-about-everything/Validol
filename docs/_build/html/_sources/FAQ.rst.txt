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
Also consider having a glance at :doc:`contextual validation post inspired_by/context_specific_validation` and
my take on declarative programming in general and
:doc:`declarative validation <inspired_by/declarative validation>` in particular.

How to validate json schema in Java?
-------------------------------------------------------------
b

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