package validation.leaf.is.of.value.number.negativeorzero;

import validation.result.error.Error;

import java.util.Map;

public class MustBeNegativeOrZero implements Error
{
    @Override
    public Map<String, Object> value()
    {
        return
            Map.of(
                "code", new Code().value(),
                "message", new Message().value()
            );
    }
}
