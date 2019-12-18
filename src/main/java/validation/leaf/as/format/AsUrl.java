package validation.leaf.as.format;

import validation.Validatable;
import validation.leaf.is.of.format.url.IsUrl;
import validation.leaf.is.of.format.url.MustBeValidUrl;
import validation.result.*;
import validation.result.error.Error;

import java.net.URL;

final public class AsUrl implements Validatable<URL>
{
    private Validatable<String> original;
    private Error error;

    public AsUrl(Validatable<String> original, Error error) throws Exception
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

    public AsUrl(Validatable<String> original) throws Exception
    {
        this(original, new MustBeValidUrl());
    }

    public Result<URL> result() throws Exception
    {
        Result<String> isUrlResult = new IsUrl(this.original, this.error).result();

        if (!isUrlResult.isSuccessful()) {
            return new FromNonSuccessful<>(isUrlResult);
        }

        if (!isUrlResult.value().isPresent()) {
            return new AbsentField<>(isUrlResult);
        }

        return new SuccessfulWithCustomValue<>(isUrlResult, new URL(isUrlResult.value().raw()));
    }
}
