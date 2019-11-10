package validation.leaf.is.of.structure;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.Named;
import validation.result.Result;

final public class IsJsonPrimitive implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsJsonPrimitive(Validatable<JsonElement> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
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
            return new Named<>(prevResult.name(), Either.left("This value must be a json primitive."));
        }

        return prevResult;
    }
}
