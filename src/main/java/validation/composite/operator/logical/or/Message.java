package validation.composite.operator.logical.or;

public class Message
{
    public String value()
    {
        return
            String.format(
                "Either %s or %s",
                leftResult.error(),
                rightResult.error()
            );
    }
}
