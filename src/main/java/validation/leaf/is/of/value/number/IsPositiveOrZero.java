package validation.leaf.is.of.value.number;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.as.type.AsNumber;
import validation.result.*;

// doc: all validatables are successful if the corresponding field is absent.
// If you want it to be required, specify it explicitly.
final public class IsPositiveOrZero implements Validatable<Number>
{
    private Validatable<JsonElement> original;

    public IsPositiveOrZero(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<Number> result() throws Throwable
    {
        Result<Number> asNumber = new AsNumber(this.original).result();

        if (!asNumber.isSuccessful() || !asNumber.value().isPresent()) {
            return asNumber;
        }

        if (asNumber.value().raw().doubleValue() < 0) {
            return new NonSuccessfulWithCustomError<>(asNumber, this.error());
        }

        return new SuccessfulWithCustomValue<>(asNumber, this.value(asNumber));
    }

    private Number value(Result<Number> prevResult) throws Throwable
    {
        return prevResult.value().raw();
    }

    private String error()
    {
        return "This value must be positive or zero.";
    }
}
