package validation.leaf.is.of.value.notequalto;

import validation.Validatable;
import validation.result.AbsentField;
import validation.result.FromNonSuccessful;
import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;

final public class IsNotEqualTo<T> implements Validatable<T>
{
    private Validatable<T> original;
    private T value;

    public IsNotEqualTo(Validatable<T> original, T value) throws Exception
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

        if (prevResult.value().raw().equals(this.value)) {
            return new NonSuccessfulWithCustomError<>(prevResult, new MustBeNonEqualTo<>(this.value));
        }

        return prevResult;
    }
}
