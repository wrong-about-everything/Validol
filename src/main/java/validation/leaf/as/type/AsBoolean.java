package validation.leaf.as.type;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.of.type.booolean.IsBoolean;
import validation.result.*;

final public class AsBoolean implements Validatable<Boolean>
{
    private Validatable<JsonElement> original;

    public AsBoolean(Validatable<JsonElement> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated original element can not be null");
        }

        this.original = original;
    }

    public Result<Boolean> result() throws Exception
    {
        Result<JsonElement> result = new IsBoolean(this.original).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        return new SuccessfulWithCustomValue<>(result, this.value(result));
    }

    private Boolean value(Result<JsonElement> result) throws Exception
    {
        return result.value().raw().getAsJsonPrimitive().getAsBoolean();
    }
}
