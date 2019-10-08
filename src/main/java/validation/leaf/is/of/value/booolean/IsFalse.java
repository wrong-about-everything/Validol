package validation.leaf.is.of.value.booolean;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.as.type.AsBoolean;
import validation.result.Named;
import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;
import validation.result.Unnamed;
import validation.value.Present;
import validation.value.Value;

final public class IsFalse implements Validatable<Boolean>
{
    private Validatable<JsonElement> original;

    public IsFalse(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<Boolean> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        Result<Boolean> asBoolean = new AsBoolean(this.original).result();

        if (!asBoolean.isSuccessful() || !asBoolean.value().isPresent()) {
            return asBoolean;
        }

        if (!asBoolean.value().raw().equals(false)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error());
        }

        return
            prevResult.isNamed()
                ? new Named<>(prevResult.name(), this.value(asBoolean))
                : new Unnamed<>(this.value(asBoolean))
            ;
    }

    private Either<Object, Value<Boolean>> value(Result<Boolean> prevResult) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    prevResult.value().raw()
                )
            );
    }

    private Either<Object, Value<Boolean>> error()
    {
        return Either.left("This value must be false.");
    }
}
