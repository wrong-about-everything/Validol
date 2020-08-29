package validation.composite.wellformedjson;

import com.google.gson.Gson;
import validation.result.Named;
import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;
import validation.Validatable;
import validation.result.Unnamed;
import validation.result.value.Present;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.spencerwi.either.Either;

import java.io.IOException;

final public class WellFormedJson implements Validatable<JsonElement>
{
    private Validatable<String> original;

    public WellFormedJson(Validatable<String> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<JsonElement> result() throws Exception
    {
        Result<String> originalResult = this.original.result();

        if (!originalResult.isSuccessful()) {
            return new Named<>(originalResult.name(), Either.left(originalResult.error()));
        }

        try {
            return
                originalResult.isNamed()
                    ?
                        new Named<>(
                            originalResult.name(),
                            Either.right(
                                new Present<>(
                                    new Gson().getAdapter(JsonElement.class).fromJson(originalResult.value().raw())
                                )
                            )
                        )
                    :
                        new Unnamed<>(
                            Either.right(
                                new Present<>(
                                    new Gson().getAdapter(JsonElement.class).fromJson(originalResult.value().raw())
                                )
                            )
                        );
        } catch(IOException ex) {
            return new NonSuccessfulWithCustomError<>(originalResult, new MustBeWellFormedJson());
        }
    }
}
