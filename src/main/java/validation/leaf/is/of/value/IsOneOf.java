package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.AbsentField;
import validation.result.FromNonSuccessful;
import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;
import validation.value.Value;

import java.util.List;
import java.util.stream.Collectors;

final public class IsOneOf<T> implements Validatable<T>
{
    private Validatable<T> original;
    private List<T> list;

    public IsOneOf(Validatable<T> original, List<T> list)
    {
        this.original = original;
        this.list = list;
    }

    public Result<T> result() throws Exception
    {
        Result<T> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!this.list.contains(prevResult.value().raw())) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        return prevResult;
    }

    private Either<Object, Value<T>> error()
    {
        return
            Either.left(
                String.format(
                    "This value must be one of the following: %s.",
                    this.list.stream().map(v -> v.toString()).collect(Collectors.joining(", "))
                )
            );
    }
}
