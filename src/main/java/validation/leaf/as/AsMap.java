package validation.leaf.as;

import validation.result.Named;
import validation.result.Result;
import validation.result.Unnamed;
import validation.Validatable;
import validation.value.Present;
import validation.value.Value;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

import java.util.Map;
import java.util.stream.Collectors;

final public class AsMap implements Validatable<Map<String, Object>>
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
