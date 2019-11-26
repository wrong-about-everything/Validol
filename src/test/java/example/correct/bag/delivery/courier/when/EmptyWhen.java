package example.correct.bag.delivery.courier.when;

import example.correct.bag.delivery.courier.When;
import validation.result.value.Absent;
import validation.result.value.Value;

import java.util.Date;

public class EmptyWhen implements When
{
    public Value<Date> date()
    {
        return new Absent<>();
    }
}
