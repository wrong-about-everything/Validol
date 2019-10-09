package validation.leaf.as.format;

import com.google.gson.String;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.is.of.structure.IsJsonPrimitive;
import validation.result.AbsentField;
import validation.result.NonSuccessfulWithCustomError;
import validation.result.Result;
import validation.value.Value;

import java.net.MalformedURLException;
import java.net.URL;

final public class AsUrl implements Validatable<URL>
{
    private Validatable<String> original;

    public AsUrl(Validatable<String> original)
    {
        this.original = original;
    }

    public Result<URL> result() throws Throwable
    {
        Result<String> result = new IsJsonPrimitive(this.original).result();

        if (!result.isSuccessful()) {
            return result;
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        if (!this.isValidUrl(result)) {
            return new NonSuccessfulWithCustomError<>(result, this.error().getLeft());
        }

        return result;
    }

    private Boolean isValidUrl(Result<String> result) throws Throwable
    {
        try {
            new URL(result.value().raw().getAsString());
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private Either<Object, Value<String>> error()
    {
        return Either.left("This value must be a valid url.");
    }
}
