Validol Quick start
=====================

Let's start with an example of what `Validol <https://https://github.com/wrong-about-everything/Validol/>`_ can do.
Here I'll describe features that will be enough for you in most of the cases.

Consider the following JSON schema:

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

This is a request that clearly has something to do with food order delivery. There must be plenty of things to validate before an order will be registered.
First, take a really deep breath and have a glance at the whole picture.

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

Fancy some line-by-line analysis? Here it goes:

| ``Lines 1-4``: check whether the input request data represents well-formed json. Otherwise, fail fast and return a corresponding error.
| ``Line 5``: if json is well-formed , a closure is invoked, and json data is passed.
| ``Line 6``: json structure is validated. Higher-level structure is an unnamed block of named entities.
| It corresponds to higher level json request with the following keys ``guest``, ``bag``, ``delivery``, ``payment``, and ``source``.
| ``Line 7``: A list of validatable elements, the ones corresponding to respective higher-level keys.
| ``Line 11``: The first one is ``guest``;
| ``Line 10``: and it's required.
| ``Line 9``: It must be a json object -- neither a primitive, nor an array.
| ``Line 8``: If any of previous checks fail (either there is no such key ``guest`` or it's not a json object), then do it fast, don't dive any deeper. Move on to the next element.
| ``Line 14``: If all previous conditions are satisfied, closure is invoked. The single argument is a json object corresponding to ``guest`` element.
| ``Lines 15-16``: A block named ``guest`` consists of other named entities.
| ``Line 20``: The first one is an email;
| ``Line 19``: it's required;
| ``Line 18``: and should be represented as string.
| ``Line 25``: The second one is an name;
| ``Line 24``: it's required as well;
| ``Line 23``: and should be represented as string either.
| ``Line 29``: If all validatable elements in current named block are valid, then an object of class ``Guest`` will be returned.
| ``Lines 32-35``: Already familiar fast-failing block named ``items``. Mind the absense of structure validation. If ``items`` element will be a string, subsequent validation breaks not-so-gracefully.
| ``Line 36``: If ``items`` block is present, closure is invoked.
