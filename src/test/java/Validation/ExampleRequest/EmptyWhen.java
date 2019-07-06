package Validation.ExampleRequest;

import Validation.Value.Absent;
import Validation.Value.Value;

import java.util.Date;

public class EmptyWhen implements When
{
    public Value<Date> date()
    {
        return new Absent<>();
    }
}
