package Validation.ExampleRequest;

public interface Delivery
{
    public Boolean isByCourier();

    public Where where() throws Throwable;
}
