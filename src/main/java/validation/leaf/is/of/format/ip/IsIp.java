package validation.leaf.is.of.format.ip;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.*;
import validation.result.value.Value;
import java.net.InetAddress;
import java.net.UnknownHostException;

final public class IsIp implements Validatable<String>
{
    private Validatable<String> original;

    public IsIp(Validatable<String> original) throws Exception
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

        if (!this.isValidIp(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, new InvalidIp());
        }

        return prevResult;
    }

    private Boolean isValidIp(Result<String> result) throws Exception
    {
        try {
            InetAddress.getByName(result.value().raw());
            return true;
        }  catch (UnknownHostException e) {
            return false;
        }
    }
}
