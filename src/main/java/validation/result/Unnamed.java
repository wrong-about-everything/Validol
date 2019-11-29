package validation.result;

import validation.result.error.Error;
import validation.result.value.Value;
import com.spencerwi.either.Either;

final public class Unnamed<T> implements Result<T>
{
    private Either<Error, Value<T>> value;

    public Unnamed(Either<Error, Value<T>> value) throws Exception
    {
        if (value == null) {
            throw new Exception("Value can not be null");
        }
        this.value = value;
    }

    @Override
    public Boolean isSuccessful()
    {
        return this.value.isRight();
    }

    @Override
    public Value<T> value() throws Exception
    {
        if (!this.isSuccessful()) {
            throw new Exception("No raw exists on a non-successful element");
        }

        return this.value.getRight();
    }

    @Override
    public Error error() throws Exception {
        if (this.isSuccessful()) {
            throw new Exception("No error exists on a successful element");
        }

        return this.value.getLeft();
    }

    @Override
    public Boolean isNamed()
    {
        return false;
    }

    @Override
    public String name() throws Exception
    {
        throw new Exception("Unnamed result does not have a name");
    }
}
