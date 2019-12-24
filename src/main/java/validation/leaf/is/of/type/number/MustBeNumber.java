package validation.leaf.is.of.type.number;

import validation.result.error.Error;

import java.util.Map;

final public class MustBeNumber implements Error
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
