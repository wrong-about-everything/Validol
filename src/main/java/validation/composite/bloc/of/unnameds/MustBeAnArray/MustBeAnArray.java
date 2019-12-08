package validation.composite.bloc.of.unnameds.MustBeAnArray;

import validation.result.error.Error;

import java.util.Map;

public class MustBeAnArray implements Error
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
