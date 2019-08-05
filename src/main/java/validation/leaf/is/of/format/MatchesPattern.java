package validation.leaf.is.of.format;

import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.as.AsString;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

// TODO: Should "Is" validatables return Result<String> or Result<JsonElement>?
// Probably every time I use "Is" validatable that's implied to cast json type to a concrete, in reality I should've used an "As" validatable
final public class MatchesPattern implements Validatable<String>
{
    private Validatable<JsonElement> original;
    private Pattern pattern;

    public MatchesPattern(Validatable<JsonElement> original, Pattern pattern)
    {
        this.original = original;
        this.pattern = pattern;
    }

    public Result<String> result() throws Throwable
    {
        Result<String> result = new AsString(this.original).result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        if (!pattern.matcher(result.value().raw()).matches()) {
            return new NonSuccessfulWithCustomError<>(result, this.error());
        }

        return result;
    }

    private String error()
    {
        return
            String.format(
                "This value must match a pattern %s",
                this.pattern.toString()
            );
    }
}
