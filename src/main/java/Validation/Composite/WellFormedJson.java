package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.spencerwi.either.Either;

final public class WellFormedJson implements Validatable<JsonElement>
{
    private Validatable<String> original;

    public WellFormedJson(Validatable<String> validatable)
    {
        this.original = validatable;
    }

    public Result<JsonElement> result() throws Throwable
    {
        Result<String> originalResult = this.original.result();

        if (!originalResult.isSuccessful()) {
            return new Named<>(originalResult.name(), Either.left(originalResult.error()));
        }

        try {
            return
                new Named<>(
                    originalResult.name(),
                    Either.right(
                        new Present<>(
                            new JsonParser().parse(originalResult.value().raw())
                                .getAsJsonObject()
                        )
                    )
                );
        } catch(JsonSyntaxException ex) {
            return new Named<>(originalResult.name(), Either.left("This is an invalid json"));
        }
    }
}
