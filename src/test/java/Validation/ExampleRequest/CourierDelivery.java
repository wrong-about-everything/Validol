package Validation.ExampleRequest;

public class CourierDelivery implements Delivery
{
    private Where where;

    public CourierDelivery(Where where)
    {
        this.where = where;
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
}
