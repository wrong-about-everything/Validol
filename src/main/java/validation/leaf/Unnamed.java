package validation.leaf;

import validation.result.Result;
import validation.Validatable;
import validation.result.error.Error;
import validation.result.value.Value;
import com.spencerwi.either.Either;

final public class Unnamed<T> implements Validatable<T>
{
    private Either<Error, Value<T>> value;

    public Unnamed(Either<Error, Value<T>> value) throws Exception
    {
        if (value == null) {
            throw new Exception("Value can not be null");
        }

        this.value = value;
    }

    public Result<T> result() throws Exception
    {
        return new validation.result.Unnamed<>(this.value);
    }
}
