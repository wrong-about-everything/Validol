package validation.leaf.as.type;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.of.type.number.IsNumber;
import validation.leaf.is.of.type.number.MustBeNumber;
import validation.result.*;
import validation.result.error.Error;

final public class AsNumber implements Validatable<Number>
{
    private Validatable<JsonElement> original;
    private Error error;

    public AsNumber(Validatable<JsonElement> original, Error error) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (error == null) {
            throw new Exception("Error can not be null");
        }

        this.original = original;
        this.error = error;
    }

    public AsNumber(Validatable<JsonElement> original) throws Exception
    {
        this(original, new MustBeNumber());
    }

    public Result<Number> result() throws Exception
    {
        Result<JsonElement> result = new IsNumber(this.original, this.error).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        return new SuccessfulWithCustomValue<>(result, result.value().raw().getAsNumber());
    }
}
