package validation.leaf.is.of.value.number;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import org.apache.commons.lang3.math.NumberUtils;
import validation.Validatable;
import validation.leaf.as.AsNumber;
import validation.result.Named;
import validation.result.Result;
import validation.result.Unnamed;
import validation.value.Present;
import validation.value.Value;

// doc: all validatables are successful if the corresponding field is absent.
// If you want it to be required, specify it explicitly.
final public class IsPositive implements Validatable<Number>
{
    private Validatable<JsonElement> original;

    public IsPositive(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<Number> result() throws Throwable
    {
        Result<Number> asNumber = new AsNumber(this.original).result();

        if (!asNumber.isSuccessful() || !asNumber.value().isPresent()) {
            return asNumber;
        }

        if (asNumber.value().raw().doubleValue() <= 0) {
            return
                asNumber.isNamed()
                    ? new Named<>(asNumber.name(), this.error())
                    : new Unnamed<>(this.error())
                ;
        }

        return
            asNumber.isNamed()
                ? new Named<>(asNumber.name(), this.value(asNumber))
                : new Unnamed<>(this.value(asNumber))
            ;
    }

    private Either<Object, Value<Number>> value(Result<Number> prevResult) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    prevResult.value().raw()
                )
            );
    }

    private Either<Object, Value<Number>> error()
    {
        return Either.left("This value must be positive.");
    }
}
