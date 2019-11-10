package validation.leaf.as.format;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.is.of.format.IsUuid;
import validation.result.*;
import validation.value.Value;

import java.util.UUID;

final public class AsUuid implements Validatable<UUID>
{
    private Validatable<String> original;

    public AsUuid(Validatable<String> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<UUID> result() throws Exception
    {
        Result<String> result = new IsUuid(this.original).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        try {
            if (!UUID.fromString(result.value().raw()).toString().equals(result.value().raw())) {
                return new NonSuccessfulWithCustomError<>(result, this.error().getLeft());
            }
        } catch (Throwable e) {
            return new NonSuccessfulWithCustomError<>(result, this.error().getLeft());
        }

        return new SuccessfulWithCustomValue<>(result, this.value(result));
    }

    private UUID value(Result<String> result) throws Exception
    {
        return UUID.fromString(result.value().raw());
    }

    private Either<Object, Value<String>> error()
    {
        return Either.left("This value must be a valid uuid.");
    }
}
