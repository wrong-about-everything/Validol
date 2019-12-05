package validation.leaf.is.absent;

import validation.result.error.Error;

import java.util.Map;

public class MustBeAbsent implements Error
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
