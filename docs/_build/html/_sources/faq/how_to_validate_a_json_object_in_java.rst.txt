How to validate a JSON object in java?
----------------------------------------

Request validation
^^^^^^^^^^^^^^^^^^^
When you implement a REST API using json, apparently, you need to :doc:`validate requests <what_is_the_best_way_to_validate_json_request_in_java>` somehow.
Using JSON Schema might be an overkill and, all in all, a tedious task. Binding validation to a data model, an approach often used in Java Spring,
:doc:`doesn't seem to be good either <../inspired_by/context_specific_validation>`: validation and data model should be independent from each other.

Response validation
^^^^^^^^^^^^^^^^^^^
Besides validating json requests, you might want to validate some third-party service json responses. Depending on whether it's successful or not,
you want different code branches to execute. From the practical point of view, there is actually no difference what to validate, be it a request or response/

What to use for json validation
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
You might want to give validol a shot. It's simple, intuitive, declarative library. Here is how a json object validation could look like:

.. code-block:: java

    {
        "discount":{
            "valid_until":"2032-05-04 00:00:00+07",
            "promo_code":"VASYA1988"
        }
    }

Here is how the actual validation code looks like:

.. code-block:: java
    :linenos:

    public class ValidatedJsonObjectRepresentingRequestOrResponse implements Validatable<JsonObjectRepresentingRequestOrResponse>
    {
        private String jsonString;
        private Connection dbConnection;

        public ValidatedJsonObjectRepresentingRequestOrResponse(String jsonString, Connection dbConnection)
        {
            this.jsonString = jsonString;
            this.dbConnection = dbConnection;
        }

        @Override
        public Result<JsonObjectRepresentingRequestOrResponse> result() throws Exception
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
                                            new IndexedValue("discount", requestJsonObject)
                                        )
                                    ),
                                    discountObject ->
                                        new NamedBlocOfNameds<>(
                                            "discount",
                                            List.of(
                                                new PromoCodeIsNotExpired(
                                                    new AsString(
                                                        new Required(
                                                            new IndexedValue("valid_until", discountObject)
                                                        )
                                                    )
                                                ),
                                                new PromoCodeIsNotAlreadyRedeemed(
                                                    new PromoCodeContainsBothLettersAndDigits(
                                                        new Required(
                                                            new IndexedValue("promo_code", discountObject)
                                                        )
                                                    ),
                                                    this.dbConnection
                                                )
                                            ),
                                            Discount.class
                                        )
                                )
                            ),
                            JsonObjectRepresentingRequestOrResponse.class
                        )
                )
                    .result()
                ;
        }
    }

Let's see what's going on here, line by line:

| ``Line 1`` Declaration of ``ValidatedJsonObjectRepresentingRequestOrResponse``.
| ``Line 6`` Its constructor accepts raw json string. It might be either an incoming request or received response, or pretty much anything else.
| ``Line 13``: Validation starts when this method is invoked.
| ``Lines 16``: The higher-level validation object is ``FastFail`` block. If the first argument is invalid, an error is returned right away.
| ``Lines 17-19``: json is checked whether it's well-formed or not. If the latter, validation fails fast and returns a corresponding error.
| ``Line 20``: if json is well-formed, a closure is invoked, and json data is passed as its single argument.
| ``Line 21``: json data is validated. Its structure is an unnamed block of named blocks. It corresponds to a JSON Object.
| ``Line 26``: The first (and the only) block is called ``discount``.
| ``Line 25``: It's required.
| ``Line 24``: It must be a json object.
| ``Line 23``: If not, an error will be returned right away because it's a ``FailFast`` object.
| ``Line 29``: Otherwise, a closure is invoked.
| ``Line 30``: Discount block is a named block consisting of other named entries -- objects or scalars.
| ``Line 36``: The first one is called ``valid_until``
| ``Line 35``: It's required.
| ``Line 34``: And represented as a string.
| ``Line 33``: Finally, check that it's not expired.
| ``Line 43``: Second parameter is called ``promo_code``.
| ``Line 42``: It's required as well.
| ``Line 41``: It must contain both letters and digits.
| ``Line 40``: And it should not be already redeemed. This fact is certainly persisted in our database, hence ...
| ``Line 46``:  ... ``this.dbConnection`` parameter.
| ``Line 49``: If all previous validation checks are successful, an object of class ``Discount`` is created.
| ``Line 53``: Finally, ``JsonObjectRepresentingRequestOrResponse`` is created and returned.

Here is how a calling code looks when validation is successful:

.. code-block:: java

    Result<JsonObjectRepresentingRequestOrResponse> result = new ValidatedJsonObjectRepresentingRequestOrResponse(jsonRequestString).result();
    result.isSuccessful();
    result.value().raw().discount().promoCode(); // VASYA1988
