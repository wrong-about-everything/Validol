package validation.leaf.is.of.format;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import org.apache.commons.validator.routines.EmailValidator;
import validation.Validatable;
import validation.leaf.is.of.structure.IsJsonPrimitive;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;

final public class IsEmail implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsEmail(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<JsonElement> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!new IsJsonPrimitive(this.original).result().isSuccessful()) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        if (!this.isValidEmail(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        return
            prevResult.isNamed()
                ? new Named<>(prevResult.name(), this.value(prevResult))
                : new Unnamed<>(this.value(prevResult))
            ;
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
