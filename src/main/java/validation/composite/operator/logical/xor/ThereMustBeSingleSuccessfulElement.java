package validation.composite.operator.logical.xor;

import validation.result.error.Error;

import java.util.Map;

final public class ThereMustBeSingleSuccessfulElement implements Error
{
    private String message;

    public ThereMustBeSingleSuccessfulElement(String message)
    {
        this.message = message;
    }

    @Override
    public Map<String, Object> value()
    {
        return
            Map.of(
                "code", new Code().value(),
                "message", new Message(this.message).value()
            );
    }
}
