package validation.leaf.is.of.value;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.as.AsBoolean;
import validation.leaf.is.of.structure.IsJsonPrimitive;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;

final public class IsTrue implements Validatable<Boolean>
{
    private Validatable<JsonElement> original;

    public IsTrue(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<Boolean> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        Result<Boolean> isBoolean = new AsBoolean(this.original).result();

        if (!isBoolean.isSuccessful()) {
            return isBoolean;
        }

        if (!isBoolean.value().raw().equals(true)) {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), this.error())
                    : new Unnamed<>(this.error())
                ;
        }

        return
            prevResult.isNamed()
                ? new Named<>(prevResult.name(), this.value(isBoolean))
                : new Unnamed<>(this.value(isBoolean))
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
        return Either.left("This value must be true.");
    }
}
