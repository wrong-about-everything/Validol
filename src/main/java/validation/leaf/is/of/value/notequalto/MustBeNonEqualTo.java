package validation.leaf.is.of.value.notequalto;

import validation.result.error.Error;
import java.util.Map;

final public class MustBeNonEqualTo<T> implements Error
{
    private T value;

    public MustBeNonEqualTo(T value)
    {
        this.value = value;
    }

    @Override
    public Map<String, Object> value()
    {
        return
            Map.of(
                "code", new Code().value(),
                "message", new Message<>(this.value).value()
            );
    }
}
