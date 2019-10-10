package validation.leaf.is.of.type;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.is.of.structure.IsJsonPrimitive;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;

final public class IsBoolean implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsBoolean(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<JsonElement> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!new IsJsonPrimitive(this.original).result().isSuccessful()) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        if (!this.isBoolean(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        return new SuccessfulWithCustomValue<>(prevResult, this.value(prevResult));
    }

    private JsonElement value(Result<JsonElement> prevResult) throws Throwable
    {
        return prevResult.value().raw();
    }

    private Either<Object, Value<JsonElement>> error()
    {
        return Either.left("This value must be a boolean.");
    }

    private Boolean isBoolean(Result<JsonElement> prevResult) throws Throwable
    {
        return
            prevResult.value().raw().toString()
                .equals(
                    String.valueOf(prevResult.value().raw().getAsBoolean())
                );
    }
}
