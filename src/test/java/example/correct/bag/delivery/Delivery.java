package example.correct.bag.delivery;

import example.correct.bag.delivery.courier.when.WhenData;
import example.correct.bag.delivery.courier.where.Where;

public interface Delivery
{
    public Boolean isByCourier();

    public Where where() throws Throwable;

    public WhenData when() throws Throwable;
}
