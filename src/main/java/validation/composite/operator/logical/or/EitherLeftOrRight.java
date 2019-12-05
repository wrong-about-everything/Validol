package validation.composite.operator.logical.or;

import validation.result.error.Error;

import java.util.Map;

public class EitherLeftOrRight implements Error
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
