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
        public Result<JsonElement> result() throws Exception
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
in a declarative fashion. For a line-by-line example take a look at :doc:`Quick-start <../quick_start>` section.
Also consider having a glance at :doc:`contextual validation post <../inspired_by/context_specific_validation>` and
my take on declarative programming in general and
:doc:`declarative validation <../inspired_by/declarative_validation>` in particular, there you can find some more examples.
