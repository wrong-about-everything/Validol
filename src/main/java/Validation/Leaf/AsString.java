package Validation.Leaf;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

public class AsString implements Validatable<String>
{
    private Validatable<JsonElement> validatable;

    public AsString(Validatable<JsonElement> validatable)
    {
        this.validatable = validatable;
    }

    public Result<String> result() throws Exception
    {
        Result<JsonElement> result = this.validatable.result();

        if (!result.value().isJsonPrimitive()) {
            // @todo: informative message
            throw new Exception();
        }

        return new Named<>(result.name(), Either.right(result.value().getAsJsonPrimitive().getAsString()));
    }
}
