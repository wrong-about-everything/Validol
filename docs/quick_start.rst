Validol Quick start
=====================

Let's start with an example of what `Validol <https://github.com/wrong-about-everything/Validol/>`_ can do.
Here I'll describe features that will be enough for you in most of the cases.

Example
--------
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

The whole validation from a bird's view
------------------------------------------

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

Now breath out. It's not that scary.

Line-by-line analysis
-----------------------
Fancy some line-by-line analysis? Here it goes:

| ``Lines 1-4``: check whether the input request data represents well-formed json. Otherwise, fail fast and return a corresponding error.
| ``Line 5``: if json is well-formed , a closure is invoked, and json data is passed.
| ``Line 6``: json structure is validated. Higher-level structure is an unnamed block of named entities.
| It corresponds to higher level json request with the following keys ``guest``, ``bag``, ``delivery``, ``payment``, and ``source``.
| ``Line 7``: A list of original elements, the ones corresponding to respective higher-level keys.
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
| ``Line 29``: If all original elements in current named block are valid, then an object of class ``Guest`` will be returned.
| The corresponding class has two arguments: `email`, which must be String, and `name`, which must be String as well.
| ``Lines 32-35``: Already familiar fast-failing block named ``items``. Mind the absense of structure validation.
| If ``items`` element will be a string, subsequent validation breaks not-so-gracefully.
| ``Line 36``: If ``items`` block is present, closure is invoked. The single argument represents ``items`` json element.
| ``Line 37``: Here the description of ``items`` element starts. For now we can see that it's a named block
| which consists of an arbitrary number of unnamed elements.
| ``Line 38``: The block mentioned above is named ``items``.
| ``Line 39``: As the second argument, its corresponding json element is passed, which should be a json array. It's a good idea
| to reinforce this knowledge
| ``Line 40``: Since I don't know the exact structure, I need a mapping function, taking a json array and returning an array of concrete objects.
| ``Line 41``: Each array element corresponds to an unnamed block.
| ``Line 42``: The first argument is a list of array values.
| ``Line 45``: In this particular case, there is a single ``id`` element.
| ``Line 44``: It's required,
| ``Line 43``: ... and should be represented as integer.
| ``Line 49``: If everything's ok, an object of the ``Item`` class is created. It has a single argument: `id`, and it must be integer.
| ``Line 51``: If validation for every block is correct, then the ``Items`` object is created, consisting of ``Item`` objects list passed as constructor argument.
| ``Lines 54-57``: On to the next element, ``delivery``. First goes usual ``FastFail`` block.
| ``Line 58``: Closure is invoked.
| ``Line 59``: The structure of ``delivery`` block depends on what type of delivery it is. It's denoted by the ``type_id`` key.
| Hence the block name is ``SwitchTrue``, switching between whether ``type_id`` is 10, 20, or something else.
| `Check tests for more examples <https://github.com/wrong-about-everything/Validol/blob/master/src/test/java/validation/composite/conditional/switcz/SwitchTrueTest.java>`_.
| ``Line 60``: The name of the block is ``delivery``.
| ``Line 61``: The list of original elements is passed.
| ``Line 62``: The first original element is specific to a concrete condition. It could pretty much anything,
| for example checking that ``type_id`` equals to 20.
| ``Line 65``: Here it's just stub always returning true.
| ``Line 66``: If the previous condition is satisfied, original passed in a second argument validates ``deliveryJsonElement``.
| It's an unnamed block of named elements.
| ``Line 67``: The list of original elements.
| ``Line 69``: The first one is a ``where`` block.
| ``Line 68``: It's wrapped into already familiar ``FastFail`` thing.
| ``Line 70``: The second argument is a closure. It's first argument is a ``where`` json object.
| I was  a bit sloppy and haven't reinforced this knowledge with ``IsJsonObject`` object, but I should've done it.
| ``Line 71``: Here goes the named block of named elements.
| ``Line 72``: ``where`` is its name.
| ``Line 73``: The second argument is a list of all elements.
| ``Line 76``: The first element is ``street``.
| ``Line 75``: It's required.
| ``Line 74``: And should be represented as string.
| ``Line 81``: The second one is ``building``.
| ``Line 80``: It's required as well.
| ``Line 79``: And should be represented as an integer.
| ``Line 85``: If all previous checks are successful, an ``Where`` object is created.
| It's first argument is `street`, which must be a String; the second one is `building`, which must be an integer.
| ``Lines 88-106``: It's pretty much the same with ``where`` block.
| ``Line 107``: If all original elements are correct, an object of ``CourierDelivery`` class is created.
| It has two arguments: `where`, which should be of ``Where`` class, and `when`, which should be of ``When`` class.
| ``Lines 113-117``: The last element is ``source``. All the basic standard checks are in place.
| ``Line 119``: Finally, an object of class ``OrderRegistrationRequestData`` is created.
| ``Line 122``: An object of class ``Result<OrderRegistrationRequestData>`` is returned.
| If the result is successful, it contains an ``OrderRegistrationRequestData`` object. Otherwise, there is an ``Object`` of arbitrary structure.
| Typically, you, backend engineer, shouldn't even have a clue of its structure -- your system's frontend should.

Learn by doing!
-----------------
The quickest way to get the taste of this declarative style is to try it out for yourself.

If you use Maven, here's how you can install it:

.. code-block:: java

    <dependency>
        <groupId>com.github.wrong-about-everything</groupId>
        <artifactId>Validol</artifactId>
        <version>0.2.0</version>
    </dependency>

If you use any other dependency manager, you can find the instructions you need `here <https://search.maven.org/artifact/com.github.wrong-about-everything/Validol/0.2.0/jar>`_.
Please make sure you're installing the latest version!

Have fun!