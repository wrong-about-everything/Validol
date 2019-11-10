package validation.result;

import validation.value.Value;

final public class NonSuccessfulWithCustomError<T, R> implements Result<R>
{
    private Result<T> result;
    private Object error;

    public NonSuccessfulWithCustomError(Result<T> result, Object error) throws Exception
    {
        if (result == null) {
            throw new Exception("Result can not be null");
        }
        if (error == null) {
            throw new Exception("Error can not be null");
        }

        this.result = result;
        this.error = error;
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
        return this.error;
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
