package validation.leaf.is.required;

import validation.leaf.is.absent.MustBeAbsent;
import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;
import validation.Validatable;
import com.google.gson.JsonElement;

// doc: the field itself must be present, it might be empty though
final public class Required implements Validatable<JsonElement>
{
    private Validatable<JsonElement> validatable;

    public Required(Validatable<JsonElement> validatable) throws Exception
    {
        if (validatable == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.validatable = validatable;
    }

    public Result<JsonElement> result() throws Exception
    {
        Result<JsonElement> result = this.validatable.result();

        if (!result.isSuccessful()) {
            return result;
        }

        if (!result.value().isPresent()) {
            return new NonSuccessfulWithCustomError<>(result, new MustBeAbsent());
        }

        return result;
    }
}
