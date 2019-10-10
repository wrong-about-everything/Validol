package validation.leaf.is.of.format;

import com.google.gson.JsonElement;
import validation.Validatable;
import validation.leaf.as.type.AsString;
import validation.result.*;

import java.util.regex.Pattern;

final public class MatchesPattern implements Validatable<String>
{
    private Validatable<String> original;
    private Pattern pattern;

    public MatchesPattern(Validatable<String> original, Pattern pattern)
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
