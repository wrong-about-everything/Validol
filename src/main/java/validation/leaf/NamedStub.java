package validation.leaf;

import validation.result.Result;
import validation.Validatable;
import validation.result.value.Value;
import com.spencerwi.either.Either;

final public class NamedStub<T> implements Validatable<T>
{
    private String name;
    private Either<Object, Value<T>> value;

    public NamedStub(String name, Either<Object, Value<T>> value) throws Exception
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

    public Result<T> result() throws Exception
    {
        Result<T> result = new Unnamed<>(this.value).result();

        if (!result.isSuccessful()) {
            return new validation.result.Named<>(this.name, Either.left(result.error()));
        }

        return new validation.result.Named<>(this.name, Either.right(result.value()));
    }
}
