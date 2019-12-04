package validation.leaf.is.of.value.equalto.genericvalue;

public class Message<T>
{
    private T value;

    public Message(T value)
    {
        this.value = value;
    }

    public String value()
    {
        return String.format("This value must be equal to %s.", this.value);
    }
}
