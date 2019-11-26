package validation.leaf.is.of.type;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.of.structure.IsJsonPrimitive;
import validation.result.*;

final public class IsString implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsString(Validatable<JsonElement> original) throws Exception
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

        if (!prevResult.value().raw().getAsJsonPrimitive().isString()) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error());
        }

        return new SuccessfulWithCustomValue<>(prevResult, this.value(prevResult));
    }

    private JsonElement value(Result<JsonElement> prevResult) throws Exception
    {
        return prevResult.value().raw();
    }

    private String error()
    {
        return "This value must be a string.";
    }
}
