package validation.leaf.is.of.type.string;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.of.structure.jsonprimitive.IsJsonPrimitive;
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
            return new NonSuccessfulWithCustomError<>(prevResult, new MustBeString());
        }

        return new SuccessfulWithCustomValue<>(prevResult, prevResult.value().raw());
    }
}
