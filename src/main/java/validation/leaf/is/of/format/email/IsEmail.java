package validation.leaf.is.of.format.email;

import org.apache.commons.validator.routines.EmailValidator;
import validation.Validatable;
import validation.result.*;
import validation.result.error.Error;

final public class IsEmail implements Validatable<String>
{
    private Validatable<String> original;
    private Error error;

    public IsEmail(Validatable<String> original, Error error) throws Exception
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

    public IsEmail(Validatable<String> original) throws Exception
    {
        this(original, new MustBeValidEmail());
    }

    public Result<String> result() throws Exception
    {
        Result<String> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return prevResult;
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!this.isValidEmail(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error);
        }

        return prevResult;
    }

    private Boolean isValidEmail(Result<String> prevResult) throws Exception
    {
        return EmailValidator.getInstance().isValid(prevResult.value().raw().trim());
    }
}
