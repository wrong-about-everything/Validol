package validation.leaf.is.of.value.greaterthan.genericvalue;

import validation.Validatable;
import validation.result.AbsentField;
import validation.result.FromNonSuccessful;
import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;
import validation.result.error.Error;

final public class IsGreaterThan<T extends Comparable<T>> implements Validatable<T>
{
    private Validatable<T> original;
    private T value;
    private Error error;

    public IsGreaterThan(Validatable<T> original, T value, Error error) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (value == null) {
            throw new Exception("Value to check against can not be null");
        }
        if (error == null) {
            throw new Exception("Error can not be null");
        }

        this.original = original;
        this.value = value;
        this.error = error;
    }

    public IsGreaterThan(Validatable<T> original, T value) throws Exception
    {
        this(original, value, new MustBeGreaterThan<>(value));
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
            return new NonSuccessfulWithCustomError<>(prevResult, this.error);
        }

        return prevResult;
    }
}
