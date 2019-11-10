package example.correct.bag;

import example.correct.bag.delivery.courier.When;
import example.correct.bag.delivery.courier.Where;

public interface Delivery
{
    public Boolean isByCourier();

    public Where where() throws Exception;

    public When when() throws Exception;
}
