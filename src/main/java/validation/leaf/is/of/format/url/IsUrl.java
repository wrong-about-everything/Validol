package validation.leaf.is.of.format.url;

import validation.Validatable;
import validation.result.*;
import validation.result.error.Error;

import java.net.MalformedURLException;
import java.net.URL;

final public class IsUrl implements Validatable<String>
{
    private Validatable<String> original;
    private Error error;

    public IsUrl(Validatable<String> original, Error error) throws Exception
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

    public IsUrl(Validatable<String> original) throws Exception
    {
        this(original, new MustBeValidUrl());
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

        if (!this.isValidUrl(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error);
        }

        return prevResult;
    }

    private Boolean isValidUrl(Result<String> result) throws Exception
    {
        try {
            new URL(result.value().raw());
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
