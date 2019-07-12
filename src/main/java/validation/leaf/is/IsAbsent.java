package validation.leaf.is;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.Named;
import validation.result.Result;
import validation.result.Unnamed;
import validation.value.Absent;
import validation.value.Present;
import validation.value.Value;

final public class IsAbsent<T> implements Validatable<T>
{
    private Validatable<T> original;

    public IsAbsent(Validatable<T> original)
    {
        this.original = original;
    }

    public Result<T> result() throws Throwable
    {
        Result<T> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), Either.left(prevResult.error()))
                    : new Unnamed<>(Either.left(prevResult.error()))
                ;
        }

        return
            prevResult.isNamed()
                ? new Named<>(prevResult.name(), this.value(prevResult))
                : new Unnamed<>(this.value(prevResult))
            ;
    }

    private Either<Object, Value<T>> value(Result<T> previousResult) throws Throwable
    {
        return
            previousResult.value().isPresent()
                ? Either.left("There should be no such field")
                : Either.right(previousResult.value())
            ;
    }
}
