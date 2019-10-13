package validation.leaf;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

// doc: the field itself must be present, it might be empty though
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
            return new Named<>(result.name(), Either.left("This field is obligatory"));
        }

        return result;
    }
}
