package validation.result;

import validation.value.Value;

final public class FromNonSuccessful<T, R> implements Result<R>
{
    private Result<T> result;

    public FromNonSuccessful(Result<T> result) throws Exception
    {
        if (result.isSuccessful()) {
            throw new Exception("Result must be non successful");
        }
        this.result = result;
    }

    @Override
    public Boolean isSuccessful()
    {
        return false;
    }

    @Override
    public Value<R> value() throws Exception
    {
        throw new Exception("No raw exists on a non-successful element");
    }

    @Override
    public Object error() throws Exception
    {
        return this.result.error();
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
