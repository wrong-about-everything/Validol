package validation.leaf.is.of.type.number;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import org.apache.commons.lang3.math.NumberUtils;
import validation.Validatable;
import validation.leaf.is.of.structure.jsonprimitive.IsJsonPrimitive;
import validation.result.*;
import validation.result.error.Error;
import validation.result.value.Value;

final public class IsNumber implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;
    private Error error;

    public IsNumber(Validatable<JsonElement> original, Error error) throws Exception
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

    public IsNumber(Validatable<JsonElement> original) throws Exception
    {
        this(original, new MustBeNumber());
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

        if (!this.isNumber(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error);
        }

        return new SuccessfulWithCustomValue<>(prevResult, prevResult.value().raw());
    }

    private Boolean isNumber(Result<JsonElement> prevResult) throws Exception
    {
        return NumberUtils.isParsable(prevResult.value().raw().getAsString());
    }
}
