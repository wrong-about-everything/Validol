package validation.leaf.is.of.value.generic.oneof;

import validation.result.error.Error;

import java.util.List;
import java.util.Map;

public class MustBeOneOf<T> implements Error
{
    private List<T> value;

    public MustBeOneOf(List<T> value)
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
