package validation.leaf.as.format;

import validation.Validatable;
import validation.leaf.is.of.format.ip.IsIp;
import validation.leaf.is.of.format.ip.MustBeValidIp;
import validation.result.*;
import validation.result.error.Error;
import java.net.InetAddress;

final public class AsIp implements Validatable<InetAddress>
{
    private Validatable<String> original;
    private Error error;

    public AsIp(Validatable<String> original, Error error) throws Exception
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

    public AsIp(Validatable<String> original) throws Exception
    {
        this(original, new MustBeValidIp());
    }

    public Result<InetAddress> result() throws Exception
    {
        Result<String> result = new IsIp(this.original, this.error).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        return new SuccessfulWithCustomValue<>(result, this.value(result));
    }

    private InetAddress value(Result<String> result) throws Exception
    {
        return InetAddress.getByName(result.value().raw());
    }
}
