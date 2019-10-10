package validation.leaf.is.of.value.booolean;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.as.type.AsBoolean;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;

// doc: all validatables are successful if the corresponding field is absent.
// If you want it to be required, specify it explicitly.
final public class IsTrue implements Validatable<Boolean>
{
    private Validatable<JsonElement> original;

    public IsTrue(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<Boolean> result() throws Throwable
    {
        Result<Boolean> asBoolean = new AsBoolean(this.original).result();

        if (!asBoolean.isSuccessful() || !asBoolean.value().isPresent()) {
            return asBoolean;
        }

        if (!asBoolean.value().raw().equals(true)) {
            return new NonSuccessfulWithCustomError<>(asBoolean, this.error());
        }

        return new SuccessfulWithCustomValue<>(asBoolean, this.value(asBoolean));
    }

    private Boolean value(Result<Boolean> prevResult) throws Throwable
    {
        return prevResult.value().raw();
    }

    private Either<Object, Value<Boolean>> error()
    {
        return Either.left("This value must be true.");
    }
}
