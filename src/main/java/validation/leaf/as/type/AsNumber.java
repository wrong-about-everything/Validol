package validation.leaf.as.type;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.of.type.IsNumber;
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

    public Result<Number> result() throws Throwable
    {
        Result<JsonElement> isNumberResult = new IsNumber(this.original).result();

        if (!isNumberResult.isSuccessful()) {
            return new FromNonSuccessful<>(isNumberResult);
        }

        if (!isNumberResult.value().isPresent()) {
            return new AbsentField<>(isNumberResult);
        }

        return new SuccessfulWithCustomValue<>(isNumberResult, this.value(isNumberResult));
    }

    private Number value(Result<JsonElement> result) throws Throwable
    {
        return result.value().raw().getAsNumber();
    }
}
