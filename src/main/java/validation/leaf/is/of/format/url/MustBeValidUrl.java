package validation.leaf.is.of.format.url;

import validation.result.error.Error;

import java.util.Map;

final public class MustBeValidUrl implements Error
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
