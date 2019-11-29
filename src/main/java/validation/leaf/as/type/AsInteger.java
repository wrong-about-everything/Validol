package validation.leaf.as.type;

import validation.leaf.is.of.type.integer.IsInteger;
import validation.result.*;
import validation.Validatable;
import com.google.gson.JsonElement;

final public class AsInteger implements Validatable<Integer>
{
    private Validatable<JsonElement> original;

    public AsInteger(Validatable<JsonElement> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<Integer> result() throws Exception
    {
        Result<JsonElement> result = this.original.result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!new IsInteger(this.original).result().isSuccessful()) {
            return new NonSuccessfulWithCustomError<>(result, "This value must be an integer.");
        }

        return new SuccessfulWithCustomValue<>(result, this.value(result));
    }

    private Integer value(Result<JsonElement> result) throws Exception
    {
        return Integer.parseInt(result.value().raw().toString());
    }
}
