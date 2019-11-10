package example.correct.bag.delivery.courier.where;

import example.correct.bag.delivery.courier.Where;

public class EmptyWhere implements Where
{
    public Boolean isAbsent()
    {
        return true;
    }

    public String street() throws Exception
    {
        throw new Exception("Empty where does not have a street");
    }

    public Integer building() throws Exception
    {
        throw new Exception("Empty where does not have a building");
    }
}
