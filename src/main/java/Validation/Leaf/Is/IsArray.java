package Validation.Leaf.Is;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

final public class IsArray implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsArray(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<JsonElement> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new Named<>(prevResult.name(), Either.left(prevResult.error()));
        }

        try {
            prevResult.value().raw().getAsJsonArray();
        } catch (IllegalStateException e) {
            return new Named<>(prevResult.name(), Either.left("This element should represent an array"));
        }

        return prevResult;
    }
}
