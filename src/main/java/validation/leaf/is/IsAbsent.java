package validation.leaf.is;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.*;
import validation.value.Value;

final public class IsAbsent<T> implements Validatable<T>
{
    private Validatable<T> original;

    public IsAbsent(Validatable<T> original)
    {
        this.original = original;
    }

    public Result<T> result() throws Exception
    {
        Result<T> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        return
            prevResult.isNamed()
                ? new Named<>(prevResult.name(), this.value(prevResult))
                : new Unnamed<>(this.value(prevResult))
            ;
    }

    private Either<Object, Value<T>> value(Result<T> previousResult) throws Exception
    {
        return
            previousResult.value().isPresent()
                ? Either.left("There should be no such field")
                : Either.right(previousResult.value())
            ;
    }
}
