How to parse and validate json in Java?
-------------------------------------------------------------
In this entry, we'll consider typical problems that arise when you're setting off parsing json with following validation.
At the end of it, I'll show how you can do it in concise and declarative fashion with
`Validol <https://github.com/wrong-about-everything/Validol>`_ library.

Problems
^^^^^^^^^^
Parsing JSON is usually a no-brainer. There are at least a couple of great libraries that do that:
`Jackson <https://github.com/FasterXML/jackson>`_ and `GSON <https://github.com/google/gson>`_. The problem is that
there seems to be no nice way to combine them with validation facility. That's where the following problems unfold.

Validation is often an imperative code-clutter
++++++++++++++++++++++++++++++++++++++++++++++++++
Typically, json schema validation looks like a huge,
`highly temporally coupled <https://enterprisecraftsmanship.com/posts/temporal-coupling-and-immutability/>`_
sequence of imperative instructions. In library-independent java-like pseudo-code, it looks like the following:

.. code-block:: java

    JsonObject jsonObject = JSON.parse(request);
    if (!jsonObject.has("guest")) {
        return error("guest block is required");
    }
    if (!jsonObject.get("guest").has("name")) {
        return error("guest name is required");
    }

    String name = jsonObject.get("guest").get("name");
    if (name.length > 70) {
        return error("guest name must be shorter than 70 characters");
    }
    if (name.length < 3) {
        return error("guest name must be at least 3 characters");
    }
    if (!Pattern.compile("[a-Z]+").matcher(name).matches() {
        return error("guest name must contain only alpha characters");
    }

JSON Schema could be not so desirable to deal with
+++++++++++++++++++++++++++++++++++++++++++++++++++++++
Those who want to break free from this imperative spaghetti hell often turn to quite complicated and intricate beast called
`JSON Schema <https://json-schema.org/latest/json-schema-core.html>`_. It's a specification describing the structure and
validation rules of json documents. Besides having a steep learning curve, it is a little bit too verbose.
And things get worse quickly: JSON schema definition file grows twice as fast as the validated json itself.

Among the pluses, this format is a declarative one, which is a huge step forward comparing to the previous option.
And there are `couple <https://github.com/java-json-tools/json-schema-validator>`_ of `libraries <https://github.com/everit-org/json-schema>`_ supporting it.

Solution
^^^^^^^^^^^^^^^
`Validol <https://github.com/wrong-about-everything/Validol>`_ provides both parsing and validating capabilities, as well as solves both validation problems: it is :doc:`declarative <../inspired_by/declarative_validation>` and concise.

Check out a quick example. Suppose we have a registration request. It has a couple of blocks, ``payment`` being one of them.
For brevity sake, I'll put only this one in the request:

.. code-block:: java

    {
        "payment":{
            "card_number":12345612341234,
            "expires_at":"12/29"
        }
    }

Here is how the actual validation code looks like:

.. code-block:: java
    :linenos:

    public class ValidatedRegistrationRequest implements Validatable<RegistrationRequest>
    {
        private String jsonRequestString;
        private Connection dbConnection;

        public ValidatedRegistrationRequest(String jsonRequestString, Connection dbConnection)
        {
            this.jsonRequestString = jsonRequestString;
            this.dbConnection = dbConnection;
        }

        @Override
        public Result<RegistrationRequest> result() throws Exception
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
                                            new IndexedValue("payment", requestJsonObject)
                                        )
                                    ),
                                    paymentJsonObject ->
                                        new NamedBlocOfNameds<>(
                                            "payment",
                                            List.of(
                                                new ValidThrueIsNotExpired(
                                                    new AsString(
                                                        new Required(
                                                            new IndexedValue("valid_thru", paymentJsonObject)
                                                        )
                                                    )
                                                ),
                                                new CardNumberIsNotBlacklisted(
                                                    new CardNumberSatisfiesLuhnAlgorithm(
                                                        new Required(
                                                            new IndexedValue("card_number", paymentJsonObject)
                                                        )
                                                    ),
                                                    this.dbConnection
                                                )
                                            ),
                                            Payment.class
                                        )
                                )
                            ),
                            RegistrationRequest.class
                        )
                )
                    .result()
                ;
        }
    }

Let's see what's going on here, line by line:

| ``Line 1`` Declaration of ``ValidatedRegistrationRequest``.
| ``Line 6`` Its constructor accepts not yet parsed json string. It might come from an incoming request, from received response, or from pretty much anywhere else.
| ``Line 13``: Validation starts when this method is invoked.
| ``Lines 16``: The higher-level validation object is ``FastFail`` block. If the first argument is invalid, an error is returned right away.
| ``Lines 17-19``: json is checked whether it's well-formed or not. If the latter, validation fails fast and returns a corresponding error.
| ``Line 20``: if json is well-formed, a closure is invoked, and json data is passed as its single argument.
| ``Line 21``: json data is validated. Its structure is an unnamed block of named blocks. It corresponds to a JSON Object.
| ``Line 26``: The first (and the only) block is called ``payment``.
| ``Line 25``: It's required.
| ``Line 24``: It must be a json object.
| ``Line 23``: If not, an error will be returned right away because it's a ``FailFast`` object.
| ``Line 29``: Otherwise, a closure is invoked.
| ``Line 30``: Payment block is a named block consisting of other named entries -- objects or scalars.
| ``Line 36``: The first one is called ``valid_thru``
| ``Line 35``: It's required.
| ``Line 34``: And represented as a string.
| ``Line 33``: Finally, check that it's not expired.
| ``Line 43``: Second parameter is called ``card_number``.
| ``Line 42``: It's required as well.
| ``Line 41``: It must satisfy Luhn algorithm.
| ``Line 40``: And it should not be blacklisted in our database, hence ...
| ``Line 46``:  ... ``this.dbConnection`` parameter.
| ``Line 49``: If all previous validation checks are successful, an object of class ``Payment`` is created.
| ``Line 53``: Finally, ``RegistrationRequest`` is created and returned.

Here is how a calling code looks when validation is successful:

.. code-block:: java

    Result<RegistrationRequest> result = new ValidatedRegistrationRequest(jsonRequestString).result();
    result.isSuccessful();
    result.value().raw().payment().cardNumber(); // 12345612341234

Further reading
^^^^^^^^^^^^^^^^^^^
For more detailed example, check out Validol's :doc:`quick-start <../quick_start>` entry.
Also, there are plenty of `higher-level unit tests <https://github.com/wrong-about-everything/Validol/tree/master/src/test/java/example>`_.
And finally feel free to contribute!
