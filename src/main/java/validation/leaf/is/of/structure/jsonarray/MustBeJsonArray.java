package validation.leaf.is.of.structure.jsonarray;

import validation.result.error.Error;

import java.util.Map;

final public class MustBeJsonArray implements Error
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
