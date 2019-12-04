package validation.leaf.is.of.value.generic.lessthanorequal.genericvalue;

public class Message<T>
{
    private T value;

    public Message(T value)
    {
        this.value = value;
    }

    public String value()
    {
        return String.format("This value must be less than or equal to %s.", this.value);
    }
}
