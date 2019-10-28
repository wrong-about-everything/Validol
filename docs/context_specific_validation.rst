.. toctree::

Contextual validation
------------------------------------
That is, independent of data-model and of domain objects.

Validation bound to data-model
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Most of current frameworks compel us, its users, to put validation in data model. At least, default mode for most of us
is to simply bind validation rules to specific fields in data model. What's wrong with this approach?
Consider an example where a guest registers a new food delivery order. The company behind this service, which actually cooks the order, is called SuperFood.
That's how the whole user-story looks like: Vasya as a guest visits SuperFood's site and registered an order there.


SuperFood's backend service must ensure several things before putting in a database. One of them is to ensure that either email or phone number are passed.

Now suppose another user registers an order in SuperFood, but through some aggregator service. This order doesn't differ much from the one registered on SuperFood site,
though the constraints to be enforced are different. For example, passing a phone number is essential for that aggregator, while email is optional.

Now, a third user registers an order in SuperFood, and she does it through some else aggregator service.
And for that one, passing a phone number is not needed, but email is a must.

So we have the following situation. I have a single data-model for an order, but there are at least three contexts with different set of constraints.
I can go traditional way: introduce an entity corresponding to a database row, and impose that constraints through annotations or config files or whatever way I'm get used to.
Data-model validation favors the following approach:


The primary objective is to find out *somehow* what exactly is the concrete scenario it operates within.
So implementing this situation(objective?) in the most straightforward way would look like the following:

.. code-block:: java

    class EitherEmailOrPhoneNumberArePresent
    {
        public EitherEmailOrPhoneNumberArePresent() {}

        public void check()
        {
            if (/* it's NOT order registration through site*/ ) {
                // do nothing
            }

            // check that either phone or email are present
        }
    }

.. code-block:: java

    class PhoneIsMandatoryForAggregatorOne
    {
        public PhoneIsMandatoryForAggregatorOne() {}

        public void check()
        {
            if (/* it's NOT order registration through aggregator 1*/ ) {
                // do nothing
            }

            // check that phone is present
        }
    }

.. code-block:: java

    class EmailIsMandatoryForAggregatorTwo
    {
        public PhoneIsMandatoryForAggregatorOne() {}

        public void check()
        {
            if (/* it's NOT order registration through aggregator 2*/ ) {
                // do nothing
            }

            // check that email is present
        }
    }

Better: validation in domain objects
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The best: contextual validation that is specific to a concrete user story
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

