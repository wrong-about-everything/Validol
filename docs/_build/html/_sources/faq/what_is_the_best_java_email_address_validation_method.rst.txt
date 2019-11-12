What is the best Java email address validation method?
-------------------------------------------------------------
Use `Validol <https://github.com/wrong-about-everything/Validol>`_'s
``IsEmail`` `class <https://github.com/wrong-about-everything/Validol/blob/master/src/main/java/validation/leaf/is/of/format/IsEmail.java>`_.

As usual, our example domain is order registration. Here is a request that we should validate:

.. code-block:: JSON

    {
       "guest":{
          "email":"vasya@belov.com"
       }
    }

If everything's successful, I want to have a bag of data with methods corresponding to blocks and fields in json request, like that:

.. code-block:: java

    Result<OrderRegistrationRequestData> result = new ValidatedOrderRegistrationRequest(jsonRequest).result();

    result.isSuccessful(); // true
    result.value().raw().guest().email(); // vasya@belov.com

In a course of a validation process, I want to make sure that ``guest`` is a valid json object.
That's how the whole thing looks like:

.. code-block:: java
    :linenos:

    public class ValidatedOrderRegistrationRequest implements Validatable<OrderRegistrationRequestData>
    {
        // ctor and jsonRequestString private property declaration

        @Override
        public Result<OrderRegistrationRequestData> result() throws Exception
        {
            return
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
                                    guestJsonElement ->
                                        new NamedBlocOfNameds<>(
                                            "guest",
                                            List.of(
                                                new IsEmail(
                                                    new AsString(
                                                        new Required(
                                                            new IndexedValue("email", guestJsonElement)
                                                        )
                                                    )
                                                )
                                            ),
                                            Guest.class
                                        )
                                )
                            ),
                            OrderRegistrationRequestData.class
                        )
                )
                    .result();
        }
    }
