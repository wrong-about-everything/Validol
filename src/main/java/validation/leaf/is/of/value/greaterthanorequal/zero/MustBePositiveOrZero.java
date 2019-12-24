package validation.leaf.is.of.value.greaterthanorequal.zero;

import validation.result.error.Error;

import java.util.Map;

final public class MustBePositiveOrZero implements Error
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
