package validation.leaf.as.format;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.is.of.format.IsIp;
import validation.result.*;
import validation.value.Value;

import java.net.InetAddress;

final public class AsIp implements Validatable<InetAddress>
{
    private Validatable<String> original;

    public AsIp(Validatable<String> original) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }

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

        return new SuccessfulWithCustomValue<>(result, this.value(result));
    }

    private InetAddress value(Result<String> result) throws Throwable
    {
        return InetAddress.getByName(result.value().raw());
    }

    private Either<Object, Value<String>> error()
    {
        return Either.left("This value must be a valid ip.");
    }
}
