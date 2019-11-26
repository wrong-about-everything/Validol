package validation.result;

import validation.result.value.Value;
import com.spencerwi.either.Either;

final public class Named<T> implements Result<T>
{
    private String name;
    private Either<Object, Value<T>> value;

    public Named(String name, Either<Object, Value<T>> value) throws Exception
    {
        if (name == null) {
            throw new Exception("Name can not be null");
        }
        if (value == null) {
            throw new Exception("Value can not be null");
        }

        this.name = name;
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
            throw new Exception("No value exists on a non-successful element");
        }

        return this.value.getRight();
    }

    @Override
    public Object error() throws Exception {
        if (this.isSuccessful()) {
            throw new Exception("No error exists on a successful element");
        }

        return this.value.getLeft();
    }

    @Override
    public Boolean isNamed()
    {
        return true;
    }

    @Override
    public String name() throws Exception
    {
        return this.name;
    }
}
