package validation.leaf.is.of.format.email;

import validation.result.error.Error;

import java.util.Map;

public class MustBeValidEmail implements Error
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