package validation.leaf.is.of.value.generic.equalto;

import validation.result.error.Error;

import java.util.Map;

public class MustBeEqualTo<T> implements Error
{
    private T value;

    public MustBeEqualTo(T value)
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
