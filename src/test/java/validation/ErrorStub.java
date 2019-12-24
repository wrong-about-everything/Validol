package validation;

import validation.result.error.Error;

import java.util.Map;

final public class ErrorStub implements Error
{
    private String message;

    public ErrorStub(String message)
    {
        this.message = message;
    }

    @Override
    public Map<String, Object> value()
    {
        return
            Map.of(
                "code", 123,
                "message", this.message
            );

    }
}
