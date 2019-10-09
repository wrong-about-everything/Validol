package validation.leaf.is.of.format;

import com.spencerwi.either.Either;
import org.apache.commons.validator.routines.EmailValidator;
import validation.Validatable;
import validation.result.*;
import validation.value.Value;

final public class IsEmail implements Validatable<String>
{
    private Validatable<String> original;

    public IsEmail(Validatable<String> original)
    {
        this.original = original;
    }

    public Result<String> result() throws Throwable
    {
        Result<String> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return prevResult;
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!this.isValidEmail(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        return prevResult;
    }

    private Boolean isValidEmail(Result<String> prevResult) throws Throwable
    {
        return EmailValidator.getInstance().isValid(prevResult.value().raw().trim());
    }

    private Either<Object, Value<String>> error()
    {
        return Either.left("This value must be an email.");
    }
}
