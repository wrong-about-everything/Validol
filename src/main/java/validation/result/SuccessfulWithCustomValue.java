package validation.result;

import validation.result.value.Present;
import validation.result.value.Value;

final public class SuccessfulWithCustomValue<T, R> implements Result<R>
{
    private Result<T> result;
    private R value;

    public SuccessfulWithCustomValue(Result<T> result, R value) throws Exception
    {
        if (result == null) {
            throw new Exception("Result can not be null");
        }
        if (value == null) {
            throw new Exception("Value can not be null");
        }
        if (!result.isSuccessful()) {
            throw new Exception("Result must be successful");
        }

        this.result = result;
        this.value = value;
    }

    @Override
    public Boolean isSuccessful()
    {
        return true;
    }

    @Override
    public Value<R> value() throws Exception
    {
        return new Present<>(this.value);
    }

    @Override
    public Object error() throws Exception
    {
        throw new Exception("No error exists on a successful element");
    }

    @Override
    public Boolean isNamed()
    {
        return this.result.isNamed();
    }

    @Override
    public String name() throws Exception
    {
        if (!this.isNamed()) {
            throw new Exception("Unnamed result does not have a name");
        }

        return this.result.name();
    }
}
