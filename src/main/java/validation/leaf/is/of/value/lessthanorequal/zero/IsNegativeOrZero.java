package validation.leaf.is.of.value.lessthanorequal.zero;

import validation.Validatable;
import validation.result.*;

// doc: all validatables are successful if the corresponding field is absent.
// If you want it to be required, specify it explicitly.
final public class IsNegativeOrZero implements Validatable<Number>
{
    private Validatable<Number> original;

    public IsNegativeOrZero(Validatable<Number> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<Number> result() throws Exception
    {
        Result<Number> asNumber = this.original.result();

        if (!asNumber.isSuccessful() || !asNumber.value().isPresent()) {
            return asNumber;
        }

        if (asNumber.value().raw().doubleValue() > 0) {
            return new NonSuccessfulWithCustomError<>(asNumber, new MustBeNegativeOrZero());
        }

        return new SuccessfulWithCustomValue<>(asNumber, asNumber.value().raw());
    }
}
