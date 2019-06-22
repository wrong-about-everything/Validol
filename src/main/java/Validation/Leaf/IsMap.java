package Validation.Leaf;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import java.lang.reflect.Type;
import java.util.Map;

public class IsMap implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsMap(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<JsonElement> result() throws Exception
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new Named<>(prevResult.name(), Either.left(prevResult.error()));
        }

        try {
            prevResult.value().getAsJsonObject().entrySet();
        } catch (IllegalStateException e) {
            return new Named<>(prevResult.name(), Either.left("This element should represent a map"));
        }

        return prevResult;
    }
}
