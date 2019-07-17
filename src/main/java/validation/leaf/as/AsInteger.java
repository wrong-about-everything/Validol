package validation.leaf.as;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import validation.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

final public class AsInteger implements Validatable<Integer>
{
    private Validatable<JsonElement> validatable;

    public AsInteger(Validatable<JsonElement> validatable)
    {
        this.validatable = validatable;
    }

    public Result<Integer> result() throws Throwable
    {
        Result<JsonElement> result = this.validatable.result();

        if (!result.isSuccessful()) {
            return new Named<>(result.name(), Either.left(result.error()));
        }

        if (!result.value().raw().isJsonPrimitive()) {
            return this.errorResult(result.name());
        }

        try {
            return
                new Named<>(
                    result.name(),
                    Either.right(
                        new Present<>(
                            Integer.parseInt(
                                result.value().raw().toString()
                            )
                        )
                    )
                );
        } catch (NumberFormatException e) {
            return this.errorResult(result.name());
        }
    }

    private Result<Integer> errorResult(String name)
    {
        return
            new Named<>(
                name,
                Either.left("This should be an integer")
            );
    }
}