Contextual validation
-------------------------
Here we will consider different ways to carry out validation, what is contextual validation and why it beats all the other ways.

Context-independent validation bound to data-model
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Most of current frameworks compel us, its users, to put validation in data model. At least, default mode for most of us
is to simply bind validation rules to specific fields in data model. What's wrong with this approach?

Consider an example where a guest registers a new food delivery order. The company behind this service, which actually cooks the order, is called SuperFood.
That's how the whole user-story looks like: Vasya as a guest visits SuperFood's site and registered an order there.


SuperFood's backend service must ensure several constraints before putting stuff in a database.
One of them is to ensure that either email or phone number are passed.

Now suppose another user registers an order in SuperFood, but through some aggregator service, call it AggreA. This order doesn't differ much from the one registered on SuperFood site,
though the constraints to be enforced are different. For example, passing a phone number is essential for that aggregator, while email is optional.

Now, a third user registers an order in SuperFood, and she does it through some else aggregator service, AggreB.
And for that one, passing a phone number is not needed, but email is a must.

So we have the following situation. I have a single data-model for an order, but there are at least three contexts with different set of constraints.
I can go traditional way: introduce an entity corresponding to a database row, and impose that constraints through annotations or config files or whatever way I'm get used to.
Data-model validation favors the following approach:

.. code-block:: java

    @ValidContactInfo
    public class User
    {
        private String phoneNumber;

        private String email;

        // ...
    }

Custom annotation ``ValidContactInfo`` eventually brings us to the `custom validator <https://stackoverflow.com/a/44326568/618020>`_,
something like ``ContactInfoValidator``. Its clearest implementation reflects the mental model of product-manager,
which goes like the following (in a pseudo code):

.. code-block:: java

    If order is being registered through site, then either email or phone number must be present.
    If order is being registered through AggreA, phone is required.
    If order is being registered through AggreB, email is required.

The primary objective is to find out *somehow* what exactly is the concrete scenario it operates within.

Data-model way implies that we should do that in a validator, taking fields from entity data-object.
This way I believe is the least palatable one, since we can't use the power of domain model and have to reside the validation logic
in `service classes <https://www.yegor256.com/2014/05/05/oop-alternative-to-utility-classes.html>`_.
Simplified, it roughly looks like that:

.. code-block:: java

    public class ContactInfoValidator
    {
        public boolean isValid(Order order)
        {
            if (order.getSource.equals(new Site())) {
                return order.getPhone() != null || order.getEmail() != null;
            } else if (order.getSource.equals(new AggreA())) {
                return order.getPhone() != null;
            } else if (order.getSource.equals(new AggreB())) {
                return order.getEmail() != null;
            }

            throw new Exception("Unknown source given");
        }
    }

Arguably better: context-independent validation in domain objects
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Often times, validation logic shawn in previous example gets out of control. In this case, it probably could be more
beneficial to put it in domain object responsible for business-logic.
Besides, traditionally, it is domain code that we developers tend to test-cover first.
It could look like the following (mind the naming:
I renamed ``Order`` to ``OrderFromRequest`` to stress the difference between it and domain order):

.. code-block:: java

    public class DomainOrder
    {
        public DomainOrder(OrderFromRequest orderFromRequest, HttpTransport httpTransport, Repository repository)
        {
            // set private fields
        }

        public boolean register()
        {
            if (this.isRegisteredThroughSite() && this.isValidForRegistrationThroughSite()) {
                // business logic 1
            } else if (this.isRegisteredThroughAggreA() && this.isValidForRegistrationThroughAggreA()) {
                // business logic 2
            } else if (this.isRegisteredThroughAggreB() && this.isValidForRegistrationThroughAggreB()) {
                // business logic 3
            }
        }

        private boolean isRegisteredThroughSite()
        {
            return orderFromRequest.getSource.equals(new Site());
        }

        private boolean isValidForRegistrationThroughSite()
        {
            return orderFromRequest.getPhone() != null || orderFromRequest.getEmail() != null;
        }
    }

But the problem of collecting errors and mapping them to UI arises. To my knowledge, there is no clean solution for that.


Contextual validation that is specific to a concrete user story
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
For me, validation serves a clear purpose: to tell clients what exactly is wrong with their requests.
But what exactly should go to validation? It depends on your take on domain model. For me, objects in domain model
represent context-independent "things" that can be orchestrated by a specific scenario in any possible way. They don't
hold any context-specific constraints. They check only universal rules, the ones that simply **must** be true, otherwise
that thing simply can't be that thing. This reflects an `always-valid approach <https://enterprisecraftsmanship.com/posts/always-valid-vs-not-always-valid-domain-model/>`_,
when you simply can't create an object in an invalid state.

For example, there is such thing as courier id. It can only consist of UUID value.
And I'll definitely want to make sure that this is the case. It usually looks like the following:

.. code-block:: java

    public class CourierId
    {
        private String uuid;

        public CourierId(String uuid)
        {
            if (/*not uuid*/) {
                throw new Exception("uuid is invalid");
            }

            this.uuid = uuid;
        }
    }

Introducing its own UUID interface with a couple of implementations would be even better:

.. code-block:: java

    public class FromString implements CourierId
    {
        private UUID uuid;

        public FromString(UUID uuid)
        {
            this.uuid = uuid;
        }

        public String value()
        {
            return this.uuid.value();
        }
    }

Typically, domain model invariants are quite basic and simple. All the other, more sophisticated context-specific checks
belong to a specific controller (or Application service, or user-story). That's where `Validol <https://github.com/wrong-about-everything/Validol>`_
comes in handy. You can first check basic, format-related validations, and proceed with however complicated ones.

Consider the following JSON request:

.. code-block:: JSON

    {
        "delivery":{
           "where":{
              "building":1,
              "street":"Red Square"
           }
        }
    }

Validation could look like that:

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
                                new IndexedValue("delivery", requestJsonObject)
                            )
                        ),
                        deliveryJsonObject ->
                            new NamedBlocOfNameds<>(
                                "delivery",
                                List.of(
                                    new FastFail<>(
                                        new IndexedValue("where", deliveryJsonObject),
                                        whereJsonElement ->
                                            new AddressWithEligibleCourierDelivery<>(
                                                new ExistingAddress<>(
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
                                                    ),
                                                    this.httpTransport
                                                ),
                                                this.dbConnection
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

I admit it might look scary for anyone who sees the code for the first time and is totally unfamiliar with domain.
Fear not, things are not so complicated. Let's consider what's going on line by line.

| ``Lines 1-4``: check whether the input request data represents well-formed json. Otherwise, fail fast and return a corresponding error.
| ``Line 5``: in case of well-formed json, a closure is invoked, and json data is passed.
| ``Line 6``: json structure is validated. Higher-level structure is an unnamed block of named entities. It closely resembles a ``Map``.
| ``Line 7``: A list with a single named block is implied.
| ``Line 11``: It's called ``delivery``.
| ``Line 10``: It's required.
| ``Line 9``: It must represent a json object.
| ``Line 14``: If all previous conditions are satisfied, closure is invoked. Otherwise, this whole things fails fast and returns an appropriate error.
| ``Line 15``: A block named ``delivery`` consists of other named entities.
| ``Line 19``: Namely, ``where`` block. It's not required though.
| ``Line 20``: If it's present, closure is invoked.
| ``Line 23``: Block named ``where`` consists of other named entities.
| ``Line 28``: Namely, ``street``, which is ...
|  ``Line 27``: ... required;
|  ``Line 26``: and is represented as string.
| ``Line 33``: and ``building``, which is ...
|  ``Line 32``: required as well;
|  ``Line 31``: and should be represented as integer.
| ``Line 37``: if all previous checks are successful, an object of class ``Where`` is created. To be honest, it's not a full-fledged object. It's just a data-structure with convenient, type-hinted and IDE-autocompleted access to its fields.
| ``Line 22``: if underlying checks are passed, an address is ensured to exist. Mind the second argument, ``httpTransport``. It's for requesting some third-party service which checks an address existence.
| ``Line 21``: Aaaand, finally, we want to ensure that courier delivery is enabled in that area. We'll need a database access for that, hence ``dbConnection`` argument.
| ``Line 45``: If everything was fine, a ``CourierDelivery`` object is created. It has a single argument, ``Where``.
| ``Line 49``: Finally, ``OrderRegistrationRequestData`` is created and returned.

So that's pretty much it. This approach might (and actually does) look like an overkill with such a simple request,
though it shines with more complicated ones.

I've intentionally put all the validating code in a single class. If the data-structure is really complex,
I'd recommend to create a class per block. Check an example `here <https://github.com/wrong-about-everything/Validol/blob/master/src/test/java/example/correct/split/ValidatedOrderRegistrationRequest.java>`_.
