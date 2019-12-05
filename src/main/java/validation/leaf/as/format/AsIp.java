package validation.leaf.as.format;

import validation.Validatable;
import validation.leaf.is.of.format.ip.IsIp;
import validation.result.*;

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

    public Result<InetAddress> result() throws Exception
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

    private InetAddress value(Result<String> result) throws Exception
    {
        return InetAddress.getByName(result.value().raw());
    }
}
