package validation.leaf.is.of.value.generic.greaterthan.genericvalue;

import validation.result.error.Error;

import java.util.Map;

public class MustBeGreaterThan<T> implements Error
{
    private T value;

    public MustBeGreaterThan(T value)
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
