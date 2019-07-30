package validation.leaf.is.of.structure;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.Named;
import validation.result.Result;

final public class IsJsonPrimitive implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsJsonPrimitive(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<JsonElement> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return prevResult;
        }

        try {
            prevResult.value().raw().getAsJsonPrimitive();
        } catch (IllegalStateException e) {
            return new Named<>(prevResult.name(), Either.left("This element should represent a primitive"));
        }

        return prevResult;
    }
}
