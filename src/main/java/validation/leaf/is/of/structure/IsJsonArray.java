package validation.leaf.is.of.structure;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

final public class IsJsonArray implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsJsonArray(Validatable<JsonElement> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<JsonElement> result() throws Exception
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new Named<>(prevResult.name(), Either.left(prevResult.error()));
        }

        try {
            prevResult.value().raw().getAsJsonArray();
        } catch (IllegalStateException e) {
            return new Named<>(prevResult.name(), Either.left("This value must be a json array"));
        }

        return prevResult;
    }
}
