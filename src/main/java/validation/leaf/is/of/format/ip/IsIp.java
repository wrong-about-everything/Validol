package validation.leaf.is.of.format.ip;

import validation.Validatable;
import validation.result.*;
import validation.result.error.Error;

import java.net.InetAddress;
import java.net.UnknownHostException;

final public class IsIp implements Validatable<String>
{
    private Validatable<String> original;
    private Error error;

    public IsIp(Validatable<String> original, Error error) throws Exception
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

    public IsIp(Validatable<String> original) throws Exception
    {
        this(original, new MustBeValidIp());
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
            return new NonSuccessfulWithCustomError<>(prevResult, this.error);
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
