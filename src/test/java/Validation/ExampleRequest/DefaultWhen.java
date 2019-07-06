package Validation.ExampleRequest;

import Validation.Value.Present;
import Validation.Value.Value;

import java.util.Date;

public class DefaultWhen implements When
{
    private Date date;

    public DefaultWhen(Date date)
    {
        this.date = date;
    }

    public Value<Date> date()
    {
        return new Present<>(this.date);
    }
}
