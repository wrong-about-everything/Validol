package validation.leaf.is.of.format;

import validation.Validatable;
import validation.result.*;

import java.util.regex.Pattern;

final public class MatchesPattern implements Validatable<String>
{
    private Validatable<String> original;
    private Pattern pattern;

    public MatchesPattern(Validatable<String> original, Pattern pattern) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (pattern == null) {
            throw new Exception("Pattern can not be null");
        }

        this.original = original;
        this.pattern = pattern;
    }

    public Result<String> result() throws Exception
    {
        Result<String> result = this.original.result();

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
