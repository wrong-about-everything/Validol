package validation.leaf.is.of.format.lengthisbetween;

public class DefaultDateErrorMessage
{
    final private Integer min;
    final private Integer max;

    public DefaultDateErrorMessage(Integer min, Integer max)
    {
        this.min = min;
        this.max = max;
    }

    public String value()
    {
        return String.format("The length of this value must be between %d and %d, inclusively.", this.min, this.max);
    }
}
