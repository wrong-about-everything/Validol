package validation.result;

import validation.result.error.Error;
import validation.result.value.Value;

public interface Result<T>
{
    public Boolean isSuccessful();

    public Value<T> value() throws Exception;

    public Error error() throws Exception;

    public Boolean isNamed();

    public String name() throws Exception;
}
