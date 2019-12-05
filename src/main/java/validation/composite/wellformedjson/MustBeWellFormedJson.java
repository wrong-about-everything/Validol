package validation.composite.wellformedjson;

import validation.result.error.Error;

import java.util.Map;

public class MustBeWellFormedJson implements Error
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
