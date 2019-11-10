package validation.leaf.is.of.value.number;

import validation.Validatable;
import validation.leaf.as.type.AsNumber;
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
            return new NonSuccessfulWithCustomError<>(asNumber, this.error());
        }

        return new SuccessfulWithCustomValue<>(asNumber, this.value(asNumber));
    }

    private Number value(Result<Number> prevResult) throws Exception
    {
        return prevResult.value().raw();
    }

    private String error()
    {
        return "This value must be negative or zero.";
    }
}
