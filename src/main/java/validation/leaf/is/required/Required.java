package validation.leaf.is.required;

import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;
import validation.Validatable;
import com.google.gson.JsonElement;
import validation.result.error.Error;

// doc: the field itself must be present, it might be empty though
final public class Required implements Validatable<JsonElement>
{
    private Validatable<JsonElement> validatable;
    private Error error;

    public Required(Validatable<JsonElement> validatable, Error error) throws Exception
    {
        if (validatable == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (error == null) {
            throw new Exception("Error can not be null");
        }

        this.validatable = validatable;
        this.error = error;
    }

    public Required(Validatable<JsonElement> validatable) throws Exception
    {
        this(validatable, new MustBePresent());
    }

    public Result<JsonElement> result() throws Exception
    {
        Result<JsonElement> result = this.validatable.result();

        if (!result.isSuccessful()) {
            return result;
        }

        if (!result.value().isPresent()) {
            return new NonSuccessfulWithCustomError<>(result, this.error);
        }

        return result;
    }
}
