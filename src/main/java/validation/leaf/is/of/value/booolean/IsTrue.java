package validation.leaf.is.of.value.booolean;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.*;
import validation.result.value.Value;

// doc: all validatables are successful if the corresponding field is absent.
// If you want it to be required, specify it explicitly.
final public class IsTrue implements Validatable<Boolean>
{
    private Validatable<Boolean> original;

    public IsTrue(Validatable<Boolean> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<Boolean> result() throws Exception
    {
        Result<Boolean> asBoolean = this.original.result();

        if (!asBoolean.isSuccessful() || !asBoolean.value().isPresent()) {
            return asBoolean;
        }

        if (!asBoolean.value().raw().equals(true)) {
            return new NonSuccessfulWithCustomError<>(asBoolean, this.error());
        }

        return new SuccessfulWithCustomValue<>(asBoolean, this.value(asBoolean));
    }

    private Boolean value(Result<Boolean> prevResult) throws Exception
    {
        return prevResult.value().raw();
    }

    private Either<Object, Value<Boolean>> error()
    {
        return Either.left("This value must be true.");
    }
}
