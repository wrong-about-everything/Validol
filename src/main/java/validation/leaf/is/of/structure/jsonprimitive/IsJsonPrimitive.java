package validation.leaf.is.of.structure.jsonprimitive;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.Named;
import validation.result.Result;
import validation.result.error.Error;

final public class IsJsonPrimitive implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;
    private Error error;

    public IsJsonPrimitive(Validatable<JsonElement> original, Error error) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (error == null) {
            throw new Exception("Error can not be null");
        }

        this.original = original;
        this.error = error;
    }

    public IsJsonPrimitive(Validatable<JsonElement> original) throws Exception
    {
        this(original, new MustBeJsonPrimitive());
    }

    public Result<JsonElement> result() throws Exception
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful() || !prevResult.value().isPresent()) {
            return prevResult;
        }

        try {
            prevResult.value().raw().getAsJsonPrimitive();
        } catch (IllegalStateException e) {
            return new Named<>(prevResult.name(), Either.left(this.error));
        }

        return prevResult;
    }
}
