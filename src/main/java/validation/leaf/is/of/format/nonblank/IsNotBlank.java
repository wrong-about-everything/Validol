package validation.leaf.is.of.format.nonblank;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.of.structure.jsonprimitive.IsJsonPrimitive;
import validation.result.*;
import validation.result.error.Error;

// doc: fields exists, but its value is blank. When value is absent, it means no such field exists
final public class IsNotBlank implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;
    private Error error;

    public IsNotBlank(Validatable<JsonElement> original, Error error) throws Exception
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

    public IsNotBlank(Validatable<JsonElement> original) throws Exception
    {
        this(original, new MustNotBeBlank());
    }

    public Result<JsonElement> result() throws Exception
    {
        Result<JsonElement> prevResult = new IsJsonPrimitive(this.original).result();

        if (!prevResult.isSuccessful()) {
            return prevResult;
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (prevResult.value().raw().getAsString().length() == 0) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error);
        }

        return prevResult;
    }
}
