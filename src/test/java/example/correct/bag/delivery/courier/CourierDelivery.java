package example.correct.bag.delivery.courier;

import example.correct.bag.delivery.courier.when.EmptyWhen;
import example.correct.bag.Delivery;
import example.correct.bag.delivery.courier.where.EmptyWhere;

public class CourierDelivery implements Delivery
{
    private Where where;
    private When when;

    public CourierDelivery(Where where, When when)
    {
        this.where = where;
        this.when = when;
    }

    public CourierDelivery(Where where)
    {
        this(where, new EmptyWhen());
    }

    public CourierDelivery(When when)
    {
        this(new EmptyWhere(), when);
    }

    public CourierDelivery()
    {
        this(new EmptyWhere(), new EmptyWhen());
    }

    @Override
    public Boolean isByCourier()
    {
        return true;
    }

    @Override
    public Where where()
    {
        return this.where;
    }

    @Override
    public When when()
    {
        return this.when;
    }
}
