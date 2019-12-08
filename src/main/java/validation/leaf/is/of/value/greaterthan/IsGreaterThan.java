package validation.leaf.is.of.value.greaterthan;

import validation.Validatable;
import validation.result.AbsentField;
import validation.result.FromNonSuccessful;
import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;

final public class IsGreaterThan<T extends Comparable<T>> implements Validatable<T>
{
    private Validatable<T> original;
    private T value;

    public IsGreaterThan(Validatable<T> original, T value) throws Exception
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

        if (prevResult.value().raw().compareTo(this.value) <= 0) {
            return new NonSuccessfulWithCustomError<>(prevResult, new MustBeGreaterThan<>(this.value));
        }

        return prevResult;
    }
}
