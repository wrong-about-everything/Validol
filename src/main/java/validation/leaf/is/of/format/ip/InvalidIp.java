package validation.leaf.is.of.format.ip;

import validation.result.error.Error;

import java.util.Map;

public class InvalidIp implements Error
{
    @Override
    public Map<String, Object> value()
    {
        return
            Map.of(
                "code", new DefaultDateErrorCode().value(),
                "message", new DefaultDateErrorMessage().value()
            );
    }
}
