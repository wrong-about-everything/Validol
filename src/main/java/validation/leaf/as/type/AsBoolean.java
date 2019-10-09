package validation.leaf.as.type;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.is.of.type.IsBoolean;
import validation.result.Named;
import validation.result.FromNonSuccessful;
import validation.result.Result;
import validation.value.Absent;
import validation.value.Present;
import validation.value.Value;

final public class AsBoolean implements Validatable<Boolean>
{
    private Validatable<JsonElement> validatable;

    public AsBoolean(Validatable<JsonElement> validatable)
    {
        this.validatable = validatable;
    }

    public Result<Boolean> result() throws Throwable
    {
        Result<JsonElement> result = this.validatable.result();

        Result<JsonElement> isBoolean = new IsBoolean(this.validatable).result();
        if (!isBoolean.isSuccessful()) {
            return new FromNonSuccessful<>(isBoolean);
        }

        return
            new Named<>(
                result.name(),
                this.value(result)
            );
    }

    private Either<Object, Value<Boolean>> value(Result<JsonElement> result) throws Throwable
    {
        return
            Either.right(
                result.value().isPresent()
                    ?
                        new Present<>(
                            result.value().raw().getAsJsonPrimitive().getAsBoolean()
                        )
                    : new Absent<>()
            );
    }
}