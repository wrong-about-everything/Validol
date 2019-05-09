package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.spencerwi.either.Either;

public class WellFormedJson implements Validatable<JsonObject>
{
    private Validatable<String> original;

    public WellFormedJson(Validatable<String> validatable)
    {
        this.original = validatable;
    }

    public Result<JsonObject> result() throws Exception
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
                        new JsonParser().parse(originalResult.value())
                            .getAsJsonObject()
                        )
                );
        } catch(JsonSyntaxException ex) {
            return new Named<>(originalResult.name(), Either.left("This is an invalid json"));
        }
    }
}
