package validation.leaf;

import validation.result.Result;
import validation.Validatable;
import validation.value.Value;
import com.spencerwi.either.Either;

final public class Named<T> implements Validatable<T>
{
    private String name;
    private Either<Object, Value<T>> value;

    public Named(String name, Either<Object, Value<T>> value)
    {
        this.name = name;
        this.value = value;
    }

    public Result<T> result() throws Throwable
    {
        Result<T> result = new Unnamed<>(this.value).result();

        if (!result.isSuccessful()) {
            return new validation.result.Named<>(this.name, Either.left(result.error()));
        }

        return new validation.result.Named<>(this.name, Either.right(result.value()));
    }
}