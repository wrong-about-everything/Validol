package validation.leaf.as.format;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.as.type.AsString;
import validation.leaf.is.of.format.IsIp;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;

import java.net.InetAddress;

final public class AsIp implements Validatable<InetAddress>
{
    private Validatable<String> original;

    public AsIp(Validatable<String> original)
    {
        this.original = original;
    }

    public Result<InetAddress> result() throws Throwable
    {
        Result<String> result = new IsIp(this.original).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        try {
            return
                result.isNamed()
                    ? new Named<>(result.name(), this.value(result))
                    : new Unnamed<>(this.value(result))
                ;
        } catch (Throwable e) {
            return new NonSuccessfulWithCustomError<>(result, this.error().getLeft());
        }
    }

    private Either<Object, Value<InetAddress>> value(Result<String> result) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    InetAddress.getByName(result.value().raw())
                )
            );
    }

    private Either<Object, Value<String>> error()
    {
        return Either.left("This value must be a valid ip.");
    }
}
