package validation.leaf.is.of.value.booolean;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.as.type.AsBoolean;
import validation.result.*;
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

        return new SuccessfulWithCustomValue<>(prevResult, this.value(asBoolean));
    }

    private Boolean value(Result<Boolean> prevResult) throws Throwable
    {
        return prevResult.value().raw();
    }

    private Either<Object, Value<Boolean>> error()
    {
        return Either.left("This value must be false.");
    }
}
