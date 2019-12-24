package validation.composite.operator.logical.or;

final public class Message
{
    private String left;
    private String right;

    public Message(String left, String right)
    {
        this.left = left;
        this.right = right;
    }

    public String value()
    {
        return
            String.format(
                "Either %s or %s",
                this.left,
                this.right
            );
    }
}
