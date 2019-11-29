package validation.leaf.is.of.format.pattern;

import validation.result.error.Error;

import java.util.Map;
import java.util.regex.Pattern;

public class PatternMustBeMatched implements Error
{
    private Pattern pattern;

    public PatternMustBeMatched(Pattern pattern)
    {
        this.pattern = pattern;
    }

    @Override
    public Map<String, Object> value()
    {
        return
            Map.of(
                "code", new Code().value(),
                "message", new Message(this.pattern).value()
            );
    }
}
