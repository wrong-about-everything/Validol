package validation.leaf.is.of.format.uuid;

import validation.Validatable;
import validation.result.*;
import validation.result.error.Error;

import java.util.UUID;

final public class IsUuid implements Validatable<String>
{
    private Validatable<String> original;
    private Error error;

    public IsUuid(Validatable<String> original, Error error) throws Exception
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

    public IsUuid(Validatable<String> original) throws Exception
    {
        this(original, new MustBeValidUuid());
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

        if (!this.isValidUuid(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error);
        }

        return prevResult;
    }

    private Boolean isValidUuid(Result<String> result)
    {
        try {
            return UUID.fromString(result.value().raw()).toString().equals(result.value().raw());
        } catch (Throwable e) {
            return false;
        }
    }
}
