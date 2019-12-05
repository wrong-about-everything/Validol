package validation.leaf.is.absent;

import validation.Validatable;
import validation.result.*;

final public class IsAbsent<T> implements Validatable<T>
{
    private Validatable<T> original;

    public IsAbsent(Validatable<T> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<T> result() throws Exception
    {
        Result<T> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (prevResult.value().isPresent()) {
            return new NonSuccessfulWithCustomError<>(prevResult, new MustBeAbsent());
        }

        return prevResult;
    }
}
