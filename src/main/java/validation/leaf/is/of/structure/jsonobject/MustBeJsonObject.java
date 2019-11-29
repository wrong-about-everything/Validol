package validation.leaf.is.of.structure.jsonobject;

import validation.result.error.Error;

import java.util.Map;

public class MustBeJsonObject implements Error
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
