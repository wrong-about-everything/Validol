package example.correct.bag.delivery.courier.when;

import validation.value.Absent;
import validation.value.Value;

import java.util.Date;

public class EmptyWhenData implements WhenData
{
    public Value<Date> date()
    {
        return new Absent<>();
    }
}
