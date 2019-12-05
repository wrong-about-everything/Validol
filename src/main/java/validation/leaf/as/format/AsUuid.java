package validation.leaf.as.format;

import validation.Validatable;
import validation.leaf.is.of.format.uuid.IsUuid;
import validation.result.*;

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

        return new SuccessfulWithCustomValue<>(result, this.value(result));
    }

    private UUID value(Result<String> result) throws Exception
    {
        return UUID.fromString(result.value().raw());
    }
}
