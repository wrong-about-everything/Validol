package validation.leaf.is.of.format;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.is.of.structure.IsJsonPrimitive;
import validation.result.*;
import validation.value.Value;
import java.net.MalformedURLException;
import java.net.URL;

final public class IsUrl implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;

    public IsUrl(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<JsonElement> result() throws Throwable
    {
        Result<JsonElement> result = new IsJsonPrimitive(this.original).result();

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

    private Boolean isValidUrl(Result<JsonElement> result) throws Throwable
    {
        try {
            new URL(result.value().raw().getAsString());
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private Either<Object, Value<JsonElement>> error()
    {
        return Either.left("This value must be a valid url.");
    }
}
