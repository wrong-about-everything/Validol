package validation.leaf.as;

import validation.leaf.is.IsJsonPrimitive;
import validation.leaf.is.IsString;
import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import validation.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

final public class AsString implements Validatable<String>
{
    private Validatable<JsonElement> validatable;

    public AsString(Validatable<JsonElement> validatable)
    {
        this.validatable = validatable;
    }

    public Result<String> result() throws Throwable
    {
        Result<JsonElement> result = this.validatable.result();

        if (!result.isSuccessful()) {
            return new Named<>(result.name(), Either.left(result.error()));
        }

        if (new IsString(this.validatable).result().isSuccessful()) {
            // !result.value().raw().getAsJsonPrimitive().isString()
            throw new Exception("Use IsString validatable to make sure that underlying raw is a string");
        }

        return
            new Named<>(
                result.name(),
                Either.right(
                    new Present<>(
                        result.value().raw().getAsJsonPrimitive().getAsString()
                    )
                )
            );
    }
}
