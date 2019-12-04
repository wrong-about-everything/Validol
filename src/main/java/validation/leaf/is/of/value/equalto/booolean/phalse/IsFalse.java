package validation.leaf.is.of.value.equalto.booolean.phalse;

import validation.Validatable;
import validation.result.*;

final public class IsFalse implements Validatable<Boolean>
{
    private Validatable<Boolean> original;

    public IsFalse(Validatable<Boolean> original) throws Exception
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

        if (!asBoolean.value().raw().equals(false)) {
            return new NonSuccessfulWithCustomError<>(asBoolean, new MustBeFalse());
        }

        return new SuccessfulWithCustomValue<>(asBoolean, asBoolean.value().raw());
    }
}
