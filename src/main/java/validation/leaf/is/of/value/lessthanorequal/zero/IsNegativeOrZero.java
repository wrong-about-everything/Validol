package validation.leaf.is.of.value.lessthanorequal.zero;

import validation.Validatable;
import validation.result.*;
import validation.result.error.Error;

// doc: all validatables are successful if the corresponding field is absent.
// If you want it to be required, specify it explicitly.
final public class IsNegativeOrZero implements Validatable<Number>
{
    private Validatable<Number> original;
    private Error error;

    public IsNegativeOrZero(Validatable<Number> original, Error error) throws Exception
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

    public IsNegativeOrZero(Validatable<Number> original) throws Exception
    {
        this(original, new MustBeNegativeOrZero());
    }

    public Result<Number> result() throws Exception
    {
        Result<Number> asNumber = this.original.result();

        if (!asNumber.isSuccessful() || !asNumber.value().isPresent()) {
            return asNumber;
        }

        if (asNumber.value().raw().doubleValue() > 0) {
            return new NonSuccessfulWithCustomError<>(asNumber, this.error);
        }

        return new SuccessfulWithCustomValue<>(asNumber, asNumber.value().raw());
    }
}
