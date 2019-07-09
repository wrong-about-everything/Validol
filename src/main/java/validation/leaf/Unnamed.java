package validation.leaf;

import validation.result.Result;
import validation.Validatable;
import validation.value.Value;
import com.spencerwi.either.Either;

final public class Unnamed<T> implements Validatable<T>
{
    private Either<Object, Value<T>> value;

    public Unnamed(Either<Object, Value<T>> value)
    {
        this.value = value;
    }

    public Result<T> result()
    {
        return new validation.result.Unnamed<>(this.value);
    }
}
