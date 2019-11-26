package example.correct.bag.delivery.courier.when;

import example.correct.bag.delivery.courier.When;
import validation.result.value.Present;
import validation.result.value.Value;

import java.util.Date;

public class DefaultWhen implements When
{
    private Date date;

    public DefaultWhen(Date date)
    {
        this.date = date;
    }

    public Value<Date> date() throws Exception
    {
        return new Present<>(this.date);
    }
}
