package validation.leaf.is.of.format.lengthisbetween;

import validation.result.error.Error;

import java.util.Map;

final public class LengthMustBeBounded implements Error
{
    final private Integer min;
    final private Integer max;

    public LengthMustBeBounded(Integer min, Integer max)
    {
        this.min = min;
        this.max = max;
    }

    @Override
    public Map<String, Object> value()
    {
        return
            Map.of(
                "code", new DefaultDateErrorCode().value(),
                "message", new DefaultDateErrorMessage(this.min, this.max).value()
            );
    }
}
