package validation.leaf.is.of.value.concrete.booolean.trooe;

import validation.Validatable;
import validation.result.*;

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
            return new NonSuccessfulWithCustomError<>(asBoolean, new MustBeTrue());
        }

        return new SuccessfulWithCustomValue<>(asBoolean, asBoolean.value().raw());
    }
}
