package Validation.Leaf;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

public class AsString implements Validatable<String>
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

        if (!result.value().raw().isJsonPrimitive()) {
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
