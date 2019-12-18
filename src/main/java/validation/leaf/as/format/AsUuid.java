package validation.leaf.as.format;

import validation.Validatable;
import validation.leaf.is.of.format.uuid.IsUuid;
import validation.leaf.is.of.format.uuid.MustBeValidUuid;
import validation.result.*;
import validation.result.error.Error;

import java.util.UUID;

final public class AsUuid implements Validatable<UUID>
{
    private Validatable<String> original;
    private Error error;

    public AsUuid(Validatable<String> original, Error error) throws Exception
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

    public AsUuid(Validatable<String> original) throws Exception
    {
        this(original, new MustBeValidUuid());
    }

    public Result<UUID> result() throws Exception
    {
        Result<String> result = new IsUuid(this.original, this.error).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        return new SuccessfulWithCustomValue<>(result, this.value(result));
    }

    private UUID value(Result<String> result) throws Exception
    {
        return UUID.fromString(result.value().raw());
    }
}
