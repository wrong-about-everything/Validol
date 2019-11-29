package validation.leaf.is.of.type.booolean;

import validation.result.error.Error;

import java.util.Map;

public class MustBeBoolean implements Error
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
