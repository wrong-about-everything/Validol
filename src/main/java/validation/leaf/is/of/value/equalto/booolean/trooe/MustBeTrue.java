package validation.leaf.is.of.value.equalto.booolean.trooe;

import validation.result.error.Error;

import java.util.Map;

final public class MustBeTrue implements Error
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
