package validation.leaf.is.of.format;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import org.apache.commons.validator.routines.EmailValidator;
import validation.Validatable;
import validation.leaf.as.AsString;
import validation.leaf.is.of.structure.IsJsonPrimitive;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;

// TODO: Should "Is" validatables return Result<String> or Result<JsonElement>?
final public class IsUrl implements Validatable<String>
{
    private Validatable<JsonElement> original;

    public IsUrl(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<JsonElement> result() throws Throwable
    {
        Result<String> result = new AsString(this.original).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return result;
        }

        if (result.value().raw().length() < min || result.value().raw().length() > max) {
            return new NonSuccessfulWithCustomError<>(result, this.error().getLeft());
        }

        return result;
    }

    private Boolean isValidEmail(Result<JsonElement> prevResult) throws Throwable
    {
        return EmailValidator.getInstance().isValid(prevResult.value().raw().getAsString().trim());
    }

    private Either<Object, Value<JsonElement>> value(Result<JsonElement> prevResult) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    prevResult.value().raw()
                )
            );
    }

    private Either<Object, Value<JsonElement>> error()
    {
        return Either.left("This value must be an email.");
    }
}
