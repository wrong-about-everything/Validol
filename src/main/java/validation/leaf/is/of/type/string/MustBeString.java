package validation.leaf.is.of.type.string;

import validation.result.error.Error;

import java.util.Map;

final public class MustBeString implements Error
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
