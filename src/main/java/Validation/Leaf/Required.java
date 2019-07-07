package Validation.Leaf;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import Validation.Value.Value;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spencerwi.either.Either;

final public class Required implements Validatable<JsonElement>
{
    private Validatable<JsonElement> validatable;

    public Required(Validatable<JsonElement> validatable)
    {
        this.validatable = validatable;
    }

    public Result<JsonElement> result() throws Throwable
    {
        Result<JsonElement> result = this.validatable.result();

        if (!result.isSuccessful()) {
            return result;
        }

        if (!result.value().isPresent()) {
            return new Named<>(result.name(), Either.left("This one is obligatory"));
        }

        return result;
    }
}
