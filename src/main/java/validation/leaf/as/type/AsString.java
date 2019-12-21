package validation.leaf.as.type;

import validation.leaf.is.of.type.string.IsString;
import validation.leaf.is.of.type.string.MustBeString;
import validation.result.*;
import validation.Validatable;
import validation.result.error.Error;
import validation.result.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

// doc: If a field is marked as NotBlank, but a field is missing, validation passes. This is a global default:
// is a field is missing, then validation passes. If you want a field to be present, make it new Required() explicitly.
final public class AsString implements Validatable<String>
{
    private Validatable<JsonElement> original;
    private Error error;

    public AsString(Validatable<JsonElement> original, Error error) throws Exception
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

    public AsString(Validatable<JsonElement> original) throws Exception
    {
        this(original, new MustBeString());
    }

    public Result<String> result() throws Exception
    {
        Result<JsonElement> result = new IsString(this.original, this.error).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        return
            new Named<>(
                result.name(),
                Either.right(
                    new Present<>(
                        result.value().raw().getAsJsonPrimitive().getAsString()
                    )
                )
            );
    }
}
