package Validation.Leaf;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

public class AsInteger implements Validatable<Integer>
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
    }
}
