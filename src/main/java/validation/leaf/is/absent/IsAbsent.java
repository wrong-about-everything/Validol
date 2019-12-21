package validation.leaf.is.absent;

import validation.Validatable;
import validation.result.*;
import validation.result.error.Error;

final public class IsAbsent<T> implements Validatable<T>
{
    private Validatable<T> original;
    private Error error;

    public IsAbsent(Validatable<T> original, Error error) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (error == null) {
            throw new Exception("Error can not be null");
        }

        this.original = original;
        this.error = error;
    }

    public IsAbsent(Validatable<T> original) throws Exception
    {
        this(original, new MustBeAbsent());
    }

    public Result<T> result() throws Exception
    {
        Result<T> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (prevResult.value().isPresent()) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error);
        }

        return prevResult;
    }
}
