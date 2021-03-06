package validation.leaf.is.of.value.equalto.booolean.phalse;

import validation.result.error.Error;

import java.util.Map;

final public class MustBeFalse implements Error
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
