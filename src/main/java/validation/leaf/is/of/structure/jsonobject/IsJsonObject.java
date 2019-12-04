package validation.leaf.is.of.structure.jsonobject;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

final public class IsJsonObject implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsJsonObject(Validatable<JsonElement> original) throws Exception
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
            return prevResult;
        }

        try {
            prevResult.value().raw().getAsJsonObject().entrySet();
        } catch (IllegalStateException e) {
            return new Named<>(prevResult.name(), Either.left(new MustBeJsonObject()));
        }

        return prevResult;
    }
}