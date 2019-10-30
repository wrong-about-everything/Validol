.. toctree::

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

Better: context-independent validation in domain objects
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Often times, validation logic shawn in previous example gets out of control. In this case, it could be more
beneficial to put it in domain object responsible for business-logic. It could look like the following (mind the naming:
I renamed ``Order`` to ``OrderFromRequest``):

.. code-block:: java

    public class DomainOrder
    {
        public DomainOrder(OrderFromRequest orderFromRequest)
        {

        }

        public boolean register()
        {
            if (this.isValid()) {
                // save in db
            }
        }

        private boolean isValid()
        {
            if (orderFromRequest.getSource.equals(new Site())) {
                return orderFromRequest.getPhone() != null || orderFromRequest.getEmail() != null;
            } else if (orderFromRequest.getSource.equals(new AggreA())) {
                return orderFromRequest.getPhone() != null;
            } else if (orderFromRequest.getSource.equals(new AggreB())) {
                return orderFromRequest.getEmail() != null;
            }

            throw new Exception("Unknown source given");
        }
    }


The best: contextual validation that is specific to a concrete user story
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

