package validation.leaf.as.type;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.of.type.booolean.IsBoolean;
import validation.leaf.is.of.type.booolean.MustBeBoolean;
import validation.result.*;
import validation.result.error.Error;

final public class AsBoolean implements Validatable<Boolean>
{
    private Validatable<JsonElement> original;
    private Error error;

    public AsBoolean(Validatable<JsonElement> original, Error error) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated original element can not be null");
        }
        if (error == null) {
            throw new Exception("Error can not be null");
        }

        this.original = original;
        this.error = error;
    }

    public AsBoolean(Validatable<JsonElement> original) throws Exception
    {
        this(original, new MustBeBoolean());
    }

    public Result<Boolean> result() throws Exception
    {
        Result<JsonElement> result = new IsBoolean(this.original, this.error).result();

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
