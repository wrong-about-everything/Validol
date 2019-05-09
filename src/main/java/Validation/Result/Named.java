package Validation.Result;

import com.spencerwi.either.Either;

public class Named<T> implements Result<T>
{
    private String name;
    private Either<String, T> value;

    public Named(String name, Either<String, T> value)
    {
        this.name = name;
        this.value = value;
    }

    @Override
    public Boolean isSuccessful()
    {
        return this.value.isRight();
    }

    @Override
    public T value() throws Exception
    {
        if (!this.isSuccessful()) {
            throw new Exception("No value exists on a non-successful element");
        }

        return this.value.getRight();
    }

    @Override
    public String error() throws Exception {
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
