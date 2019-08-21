package validation.leaf.is.of.format;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.as.AsString;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;
import java.util.UUID;

final public class IsUuid implements Validatable<UUID>
{
    private Validatable<JsonElement> original;

    public IsUuid(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<UUID> result() throws Throwable
    {
        Result<String> result = new AsString(this.original).result();

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

        return
            result.isNamed()
                ? new Named<>(result.name(), this.value(result))
                : new Unnamed<>(this.value(result))
            ;
    }

    private Either<Object, Value<UUID>> value(Result<String> result) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    UUID.fromString(result.value().raw())
                )
            );
    }

    private Either<Object, Value<JsonElement>> error()
    {
        return Either.left("This value must be a valid uuid.");
    }
}
