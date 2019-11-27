package validation.leaf.is.of.format.url;

import validation.result.error.Error;

import java.util.Map;

public class InvalidUrl implements Error
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
