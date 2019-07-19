package validation.leaf.as;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.is.IsBoolean;
import validation.result.Named;
import validation.result.FromNonSuccessful;
import validation.result.Result;
import validation.value.Present;

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

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!new IsBoolean(this.validatable).result().isSuccessful()) {
            return new Named<>(result.name(), Either.left("This value must be a boolean."));
        }

        return
            new Named<>(
                result.name(),
                Either.right(
                    new Present<>(
                        result.value().raw().getAsJsonPrimitive().getAsBoolean()
                    )
                )
            );
    }
}
