package validation.leaf.as.type;

import validation.leaf.is.of.type.IsString;
import validation.result.*;
import validation.Validatable;
import validation.result.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

// doc: If a field is marked as NotBlank, but a field is missing, validation passes. This is a global default:
// is a field is missing, then validation passes. If you want a field to be present, make it new Required() explicitly.
final public class AsString implements Validatable<String>
{
    private Validatable<JsonElement> original;

    public AsString(Validatable<JsonElement> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<String> result() throws Exception
    {
        Result<JsonElement> result = this.original.result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!new IsString(this.original).result().isSuccessful()) {
            return new NonSuccessfulWithCustomError<>(result, "This value must be a string.");
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
