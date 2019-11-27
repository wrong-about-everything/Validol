package validation.leaf.is.of.format.nonblank;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.is.of.structure.IsJsonPrimitive;
import validation.result.*;

// doc: fields exists, but its value is blank. When value is absent, it means no such field exists
final public class IsNotBlank implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsNotBlank(Validatable<JsonElement> original) throws Exception
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
            return prevResult;
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (prevResult.value().raw().getAsString().length() == 0) {
            return new NonSuccessfulWithCustomError<>(prevResult, new Blank());
        }

        return prevResult;
    }
}
