.. toctree::

Context-specific validation
------------------------------------
That is, independent of data-model.
Most of current frameworks don't see the distinction between data model scope and validating scope. At least, default mode for most of us
is to bind validation to our data model.
What's wrong with this approach?
Consider an example where a guest registers a new food delivery order. The company behind this service, which actually cooks the order, is called SuperFood.
That's how the whole user-story looks like: Vasya as a guest visits SuperFood's site and registered an order there.
SuperFood's backend service must ensure several things before putting in a database. One of them is to ensure that either email or phone number is passed.

Now suppose another user registers an order in SuperFood, but through some aggregator service. This order doesn't differ much from the one registered on SuperFood site,
though the constraints to be enforced are different. For example, passing a phone number is essential for that aggregator, while email is optional.

Now, a third user registers an order in SuperFood, and she does it through some else aggregator service.
And for that one, passing a phone number is totally useless, but email is a must.

So we have the following situation. I have a single data-model for an order, but there are at least three contexts with different set of constraints.
I can go traditional way: introduce an entity corresponding to a database row, and impose that constraints through annotations or whatever way I'm get used to.
Each of them has to check somehow what concrete scenario it operates within. The most straightforward way to do that is the following:

.. code-block:: java

class EitherPhoneOrEmailArePresent
{

    public EitherPhoneOrEmailArePresent() {}

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
