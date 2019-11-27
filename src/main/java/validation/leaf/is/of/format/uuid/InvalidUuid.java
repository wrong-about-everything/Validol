package validation.leaf.is.of.format.uuid;

import validation.result.error.Error;

import java.util.Map;

public class InvalidUuid implements Error
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
