package validation.leaf.is.of.format;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.*;
import validation.value.Value;
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

    public Result<String> result() throws Throwable
    {
        Result<String> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return prevResult;
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!this.isValidIp(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        return prevResult;
    }

    private Boolean isValidIp(Result<String> result) throws Throwable
    {
        try {
            InetAddress.getByName(result.value().raw());
            return true;
        }  catch (UnknownHostException e) {
            return false;
        }
    }

    private Either<Object, Value<String>> error()
    {
        return Either.left("This value must be a valid ip.");
    }
}
