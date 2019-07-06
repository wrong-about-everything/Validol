package Validation.Leaf.Is;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Value;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

public class IsMap implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsMap(Validatable<JsonElement> original)
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
            prevResult.value().raw().getAsJsonObject().entrySet();
        } catch (IllegalStateException e) {
            return new Named<>(prevResult.name(), Either.left("This element should represent a map"));
        }

        return prevResult;
    }
}
