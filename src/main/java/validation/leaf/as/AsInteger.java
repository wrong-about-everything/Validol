package validation.leaf.as;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import validation.result.Unnamed;
import validation.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.value.Value;

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
            // todo: Extract into some class
            return
                result.isNamed()
                    ? new Named<>(result.name(), Either.left(result.error()))
                    : new Unnamed<>(Either.left(result.error()))
                ;
        }

        if (!result.value().raw().isJsonPrimitive()) {
            return this.errorResult(result);
        }

        try {
            return
                result.isNamed()
                    ?
                        new Named<>(
                            result.name(),
                            this.value(result)
                        )
                    :
                        new Unnamed<>(
                            Either.right(
                                new Present<>(
                                    Integer.parseInt(
                                        result.value().raw().toString()
                                    )
                                )
                            )
                        )
                ;
        } catch (NumberFormatException e) {
            return this.errorResult(result);
        }
    }

    private Result<Integer> errorResult(Result<JsonElement> result) throws Throwable
    {
        return
            result.isNamed()
                ? new Named<>(result.name(), Either.left("This value must be an integer."))
                : new Unnamed<>(Either.left("This value must be an integer."))
            ;
    }

    private Either<Object, Value<Integer>> value(Result<JsonElement> result) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    Integer.parseInt(
                        result.value().raw().toString()
                    )
                )
            );
    }
}
