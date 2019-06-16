package Validation.Leaf;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spencerwi.either.Either;

public class Required implements Validatable<JsonElement>
{
    private Validatable<JsonElement> validatable;

    public Required(Validatable<JsonElement> validatable)
    {
        this.validatable = validatable;
    }

    public Result<JsonElement> result() throws Exception
    {
        Result<JsonElement> result = this.validatable.result();

        if (!result.isSuccessful()) {
            return result;
        }

        if (result.value().isJsonNull()) {
            return new Named<>(result.name(), Either.left(String.format("Key %s is required, but it does not exist", result.name())));
        }

        return result;
    }
}
