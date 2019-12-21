package validation.leaf.is.of.format.pattern;

import validation.Validatable;
import validation.result.*;
import validation.result.error.Error;

import java.util.regex.Pattern;

final public class MatchesPattern implements Validatable<String>
{
    private Validatable<String> original;
    private Pattern pattern;
    private Error error;

    public MatchesPattern(Validatable<String> original, Pattern pattern, Error error) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (pattern == null) {
            throw new Exception("Pattern can not be null");
        }
        if (error == null) {
            throw new Exception("Error can not be null");
        }

        this.original = original;
        this.pattern = pattern;
        this.error = error;
    }

    public MatchesPattern(Validatable<String> original, Pattern pattern) throws Exception
    {
        this(original, pattern, new PatternMustBeMatched(pattern));
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
            return new NonSuccessfulWithCustomError<>(result, this.error);
        }

        return result;
    }
}
