package validation.leaf.is.of.type.booolean;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.of.structure.jsonprimitive.IsJsonPrimitive;
import validation.result.*;

final public class IsBoolean implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsBoolean(Validatable<JsonElement> original) throws Exception
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

        if (!this.isBoolean(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, new MustBeBoolean());
        }

        return new SuccessfulWithCustomValue<>(prevResult, prevResult.value().raw());
    }

    private Boolean isBoolean(Result<JsonElement> prevResult) throws Exception
    {
        return
            prevResult.value().raw().toString()
                .equals(
                    String.valueOf(prevResult.value().raw().getAsBoolean())
                );
    }
}
