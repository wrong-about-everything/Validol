package validation.leaf.is.of.format;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.*;
import validation.value.Value;
import java.util.UUID;

final public class IsUuid implements Validatable<String>
{
    private Validatable<String> original;

    public IsUuid(Validatable<String> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
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
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        return prevResult;
    }

    private Boolean isValidUuid(Result<String> result) throws Exception
    {
        try {
            return UUID.fromString(result.value().raw()).toString().equals(result.value().raw());
        } catch (Throwable e) {
            return false;
        }
    }

    private Either<Object, Value<String>> error()
    {
        return Either.left("This value must be a valid uuid.");
    }
}
