package validation.leaf.is.of.value.equalto.booolean.trooe;

import validation.Validatable;
import validation.result.*;
import validation.result.error.Error;

// doc: all validatables are successful if the corresponding field is absent.
// If you want it to be required, specify it explicitly.
final public class IsTrue implements Validatable<Boolean>
{
    private Validatable<Boolean> original;
    private Error error;

    public IsTrue(Validatable<Boolean> original, Error error) throws Exception
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

    public IsTrue(Validatable<Boolean> original) throws Exception
    {
        this(original, new MustBeTrue());
    }

    public Result<Boolean> result() throws Exception
    {
        Result<Boolean> asBoolean = this.original.result();

        if (!asBoolean.isSuccessful() || !asBoolean.value().isPresent()) {
            return asBoolean;
        }

        if (!asBoolean.value().raw().equals(true)) {
            return new NonSuccessfulWithCustomError<>(asBoolean, this.error);
        }

        return new SuccessfulWithCustomValue<>(asBoolean, asBoolean.value().raw());
    }
}
