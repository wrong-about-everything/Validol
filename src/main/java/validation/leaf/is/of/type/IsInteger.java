package validation.leaf.is.of.type;

import com.google.gson.JsonElement;
import validation.leaf.is.of.structure.IsJsonPrimitive;
import validation.result.*;
import validation.Validatable;
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
        Result<JsonElement> prevResult = new IsJsonPrimitive(this.original).result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!this.isInteger(prevResult)) {
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
