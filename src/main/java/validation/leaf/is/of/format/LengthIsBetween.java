package validation.leaf.is.of.format;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.as.type.AsString;
import validation.result.*;
import validation.value.Value;

final public class LengthIsBetween implements Validatable<String>
{
    private Validatable<JsonElement> original;
    private Integer min;
    private Integer max;

    public LengthIsBetween(Validatable<JsonElement> original, Integer min, Integer max)
    {
        this.original = original;
        this.min = min;
        this.max = max;
    }

    public Result<String> result() throws Throwable
    {
        Result<String> result = new AsString(this.original).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return result;
        }

        if (result.value().raw().length() < min || result.value().raw().length() > max) {
            return new NonSuccessfulWithCustomError<>(result, this.error().getLeft());
        }

        return result;
    }

    private Either<Object, Value<JsonElement>> error()
    {
        return
            Either.left(
                String.format(
                    "The length of this value must be between %d and %d, inclusively.",
                    this.min,
                    this.max
                )
            );
    }
}
