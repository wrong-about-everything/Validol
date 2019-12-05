package validation.leaf.as.type;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.of.type.number.IsNumber;
import validation.result.*;

final public class AsNumber implements Validatable<Number>
{
    private Validatable<JsonElement> original;

    public AsNumber(Validatable<JsonElement> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<Number> result() throws Exception
    {
        Result<JsonElement> result = new IsNumber(this.original).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        return new SuccessfulWithCustomValue<>(result, result.value().raw().getAsNumber());
    }
}
