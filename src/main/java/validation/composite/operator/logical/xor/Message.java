package validation.composite.operator.logical.xor;

public class Message
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
                "Either %s or %s but not both",
                this.left,
                this.right
            );
    }
}
