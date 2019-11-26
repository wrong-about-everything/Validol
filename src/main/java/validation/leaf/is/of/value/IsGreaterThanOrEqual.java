package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.AbsentField;
import validation.result.FromNonSuccessful;
import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;
import validation.result.value.Value;

final public class IsGreaterThanOrEqual<T extends Comparable<T>> implements Validatable<T>
{
    private Validatable<T> original;
    private T value;

    public IsGreaterThanOrEqual(Validatable<T> original, T value) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (value == null) {
            throw new Exception("Value to check against can not be null");
        }

        this.original = original;
        this.value = value;
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

        if (prevResult.value().raw().compareTo(this.value) < 0) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        return prevResult;
    }

    private Either<Object, Value<T>> error()
    {
        return Either.left(String.format("This value must be greater than or equal to %s.", this.value));
    }
}
