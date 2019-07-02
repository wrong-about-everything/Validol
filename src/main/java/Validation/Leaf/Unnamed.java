package Validation.Leaf;

import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Value;
import com.spencerwi.either.Either;

public class Unnamed<T> implements Validatable<T>
{
    private Either<Object, Value<T>> value;

    public Unnamed(Either<Object, Value<T>> value)
    {
        this.value = value;
    }

    public Result<T> result()
    {
        return new Validation.Result.Unnamed<>(this.value);
    }
}
