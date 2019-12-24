package validation.leaf.is.of.format.pattern;

import java.util.regex.Pattern;

final public class Message
{
    private Pattern pattern;

    public Message(Pattern pattern)
    {
        this.pattern = pattern;
    }

    public String value()
    {
        return
            String.format(
                "This value must match a pattern %s",
                this.pattern.toString()
            );
    }
}
