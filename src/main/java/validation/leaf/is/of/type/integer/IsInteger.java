package validation.leaf.is.of.type.integer;

import com.google.gson.JsonElement;
import validation.leaf.is.of.structure.jsonprimitive.IsJsonPrimitive;
import validation.result.*;
import validation.Validatable;
import validation.result.value.Value;
import com.spencerwi.either.Either;

final public class IsInteger implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsInteger(Validatable<JsonElement> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<JsonElement> result() throws Exception
    {
        Result<JsonElement> prevResult = new IsJsonPrimitive(this.original).result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!this.isInteger(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, new MustBeInteger());
        }

        return new SuccessfulWithCustomValue<>(prevResult, prevResult.value().raw());
    }

    private Boolean isInteger(Result<JsonElement> prevResult) throws Exception
    {
        try {
            Integer.parseInt(prevResult.value().raw().toString());
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
