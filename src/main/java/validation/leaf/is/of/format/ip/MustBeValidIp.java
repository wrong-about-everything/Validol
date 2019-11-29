package validation.leaf.is.of.format.ip;

import validation.result.error.Error;

import java.util.Map;

public class MustBeValidIp implements Error
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
