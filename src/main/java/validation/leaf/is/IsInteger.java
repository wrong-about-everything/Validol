package validation.leaf.is;

import com.google.gson.JsonElement;
import validation.result.*;
import validation.Validatable;
import validation.value.Absent;
import validation.value.Present;
import validation.value.Value;
import com.spencerwi.either.Either;

final public class IsInteger implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsInteger(Validatable<JsonElement> original)
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
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), Either.right(new Absent<>()))
                    : new Unnamed<>(Either.right(new Absent<>()))
                ;
        }

        if (!new IsJsonPrimitive(this.original).result().isSuccessful()) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        if (!this.isInteger(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        return
            prevResult.isNamed()
                ? new Named<>(prevResult.name(), this.value(prevResult))
                : new Unnamed<>(this.value(prevResult))
            ;
    }

    private Either<Object, Value<JsonElement>> value(Result<JsonElement> prevResult) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    prevResult.value().raw()
                )
            );
    }

    private Either<Object, Value<JsonElement>> error()
    {
        return Either.left("This value must be an integer.");
    }

    private Boolean isInteger(Result<JsonElement> prevResult) throws Throwable
    {
        try {
            Integer.parseInt(prevResult.value().raw().toString());
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
