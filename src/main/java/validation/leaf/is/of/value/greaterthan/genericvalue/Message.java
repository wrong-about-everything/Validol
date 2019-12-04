package validation.leaf.is.of.value.greaterthan.genericvalue;

public class Message<T>
{
    private T value;

    public Message(T value)
    {
        this.value = value;
    }

    public String value()
    {
        return String.format("This value must be greater than %s.", this.value);
    }
}
