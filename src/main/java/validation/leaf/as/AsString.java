package validation.leaf.as;

import validation.leaf.is.IsString;
import validation.result.*;
import validation.Validatable;
import validation.value.Absent;
import validation.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

// todo: todoc: If a field is marked as NotBlank, but a field is missing, validation passes. This is a global default:
// is a field is missing, then validation passes. If you want a field to be present, make it new Required() explicitly.
final public class AsString implements Validatable<String>
{
    private Validatable<JsonElement> validatable;

    public AsString(Validatable<JsonElement> validatable)
    {
        this.validatable = validatable;
    }

    public Result<String> result() throws Throwable
    {
        Result<JsonElement> result = this.validatable.result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!new IsString(this.validatable).result().isSuccessful()) {
            return new NonSuccessfulWithCustomError<>(result, "This value must be a string.");
        }

        // todo: create new AbsentFieldFromResult
        if (!result.value().isPresent()) {
            return
                result.isNamed()
                    ?
                        new Named<>(
                            result.name(),
                            Either.right(
                                new Absent<>()
                            )
                        )
                    :
                        new Unnamed<>(
                            Either.right(
                                new Absent<>()
                            )
                        )
                ;
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
