package Validation.Leaf;

import Validation.Result.Result;
import Validation.Validatable;
import com.spencerwi.either.Either;

public class Named<T> implements Validatable<T>
{
    private String name;
    private Either<Object, T> value;

    public Named(String name, Either<Object, T> value)
    {
        this.name = name;
        this.value = value;
    }

    public Result<T> result()
    {
        return new Validation.Result.Named<>(this.name, this.value);
    }
}
