package validation.leaf.as;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.is.of.type.IsNumber;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;

final public class AsNumber implements Validatable<Number>
{
    private Validatable<JsonElement> validatable;

    public AsNumber(Validatable<JsonElement> validatable)
    {
        this.validatable = validatable;
    }

    public Result<Number> result() throws Throwable
    {
        Result<JsonElement> result = this.validatable.result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!new IsNumber(this.validatable).result().isSuccessful()) {
            return new NonSuccessfulWithCustomError<>(result, "This value must be an Number.");
        }

        return
            result.isNamed()
                ?
                    new Named<>(
                        result.name(),
                        this.value(result)
                    )
                :
                    new Unnamed<>(
                        this.value(result)
                    )
            ;
    }

    private Either<Object, Value<Number>> value(Result<JsonElement> result) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    result.value().raw().getAsNumber()
                )
            );
    }
}
