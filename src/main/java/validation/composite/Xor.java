package validation.composite;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.Named;
import validation.result.Result;

final public class Xor implements Validatable<JsonElement>
{
    private Validatable<JsonElement> validatable;

    public Xor(Validatable<JsonElement> validatable)
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
