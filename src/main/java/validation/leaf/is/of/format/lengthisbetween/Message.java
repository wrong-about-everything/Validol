package validation.leaf.is.of.format.lengthisbetween;

final public class Message
{
    final private Integer min;
    final private Integer max;

    public Message(Integer min, Integer max)
    {
        this.min = min;
        this.max = max;
    }

    public String value()
    {
        return String.format("The length of this value must be between %d and %d, inclusively.", this.min, this.max);
    }
}
