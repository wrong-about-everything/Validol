package validation.leaf.is.of.value.notequalto;

public class Message<T>
{
    private T value;

    public Message(T value)
    {
        this.value = value;
    }

    public String value()
    {
        return String.format("This value must be non-equal to %s.", this.value);
    }
}
