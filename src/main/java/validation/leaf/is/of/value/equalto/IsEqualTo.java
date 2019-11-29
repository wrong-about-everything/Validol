package validation.leaf.is.of.value.equalto;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.*;
import validation.result.value.Value;

final public class IsEqualTo<T> implements Validatable<T>
{
    private Validatable<T> original;
    private T value;

    public IsEqualTo(Validatable<T> original, T value) throws Exception
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

        if (!prevResult.value().raw().equals(this.value)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        return prevResult;
    }

    private Either<Object, Value<T>> error()
    {
        return Either.left(String.format("This value must be equal to %s.", this.value));
    }
}
