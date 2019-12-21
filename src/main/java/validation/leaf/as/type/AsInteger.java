package validation.leaf.as.type;

import validation.leaf.is.of.type.integer.IsInteger;
import validation.leaf.is.of.type.integer.MustBeInteger;
import validation.result.*;
import validation.Validatable;
import com.google.gson.JsonElement;
import validation.result.error.Error;

final public class AsInteger implements Validatable<Integer>
{
    private Validatable<JsonElement> original;
    private Error error;

    public AsInteger(Validatable<JsonElement> original, Error error) throws Exception
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

    public AsInteger(Validatable<JsonElement> original) throws Exception
    {
        this(original, new MustBeInteger());
    }

    public Result<Integer> result() throws Exception
    {
        Result<JsonElement> result = new IsInteger(this.original, this.error).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        return new SuccessfulWithCustomValue<>(result, this.value(result));
    }

    private Integer value(Result<JsonElement> result) throws Exception
    {
        return Integer.parseInt(result.value().raw().toString());
    }
}
