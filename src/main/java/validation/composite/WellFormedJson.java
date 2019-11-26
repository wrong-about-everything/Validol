package validation.composite;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import validation.result.Unnamed;
import validation.result.value.Present;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.spencerwi.either.Either;

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
                                    new JsonParser().parse(originalResult.value().raw())
                                        .getAsJsonObject()
                                )
                            )
                        )
                    :
                        new Unnamed<>(
                            Either.right(
                                new Present<>(
                                    new JsonParser().parse(originalResult.value().raw())
                                        .getAsJsonObject()
                                )
                            )
                        )
                ;
        } catch(JsonSyntaxException ex) {
            return new Named<>(originalResult.name(), Either.left("This must be a valid json"));
        }
    }
}
