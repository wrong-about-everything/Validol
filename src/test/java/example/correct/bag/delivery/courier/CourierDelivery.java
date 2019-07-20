package example.correct.bag.delivery.courier;

import example.correct.bag.delivery.courier.when.EmptyWhenData;
import example.correct.bag.delivery.courier.when.WhenData;
import example.correct.bag.delivery.Delivery;
import example.correct.bag.delivery.courier.where.Where;

public class CourierDelivery implements Delivery
{
    private Where where;
    private WhenData whenData;

    public CourierDelivery(Where where, WhenData whenData)
    {
        this.where = where;
        this.whenData = whenData;
    }

    public CourierDelivery(Where where)
    {
        this(where, new EmptyWhenData());
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
    public WhenData when()
    {
        return this.whenData;
    }
}
