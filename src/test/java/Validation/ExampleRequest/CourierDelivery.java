package Validation.ExampleRequest;

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
