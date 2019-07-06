package Validation.Leaf.As;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Result.Unnamed;
import Validation.Validatable;
import Validation.Value.Present;
import Validation.Value.Value;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

import java.util.Map;
import java.util.stream.Collectors;

public class AsMap implements Validatable<Map<String, Object>>
{
    private Validatable<JsonElement> original;

    public AsMap(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<Map<String, Object>> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new Named<>(prevResult.name(), Either.left(prevResult.error()));
        }

        try {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), this.value(prevResult))
                    : new Unnamed<>(this.value(prevResult))
                ;
        } catch (Throwable e) {
            return new Named<>(prevResult.name(), Either.left("This element should represent a map"));
        }
    }

    private Either<Object, Value<Map<String, Object>>> value(Result<JsonElement> prevResult) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    prevResult.value().raw().getAsJsonObject().entrySet().stream()
                        .collect(
                            Collectors.toMap(
                                currentMapEntry -> currentMapEntry.getKey(),
                                currentMapEntry -> currentMapEntry.getValue()
                            )
                        )
                )
            );
    }
}
