package validation.leaf.is.of.format;

import validation.Validatable;
import validation.result.*;

final public class LengthIsBetween implements Validatable<String>
{
    private Validatable<String> original;
    private Integer min;
    private Integer max;

    public LengthIsBetween(Validatable<String> original, Integer min, Integer max) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (min == null) {
            throw new Exception("Min argument can not be null");
        }
        if (max == null) {
            throw new Exception("Max argument can not be null");
        }

        this.original = original;
        this.min = min;
        this.max = max;
    }

    public Result<String> result() throws Exception
    {
        Result<String> result = this.original.result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return result;
        }

        if (result.value().raw().length() < min || result.value().raw().length() > max) {
            return new NonSuccessfulWithCustomError<>(result, this.error());
        }

        return result;
    }

    private String error()
    {
        return
            String.format(
                "The length of this value must be between %d and %d, inclusively.",
                this.min,
                this.max
            );
    }
}
