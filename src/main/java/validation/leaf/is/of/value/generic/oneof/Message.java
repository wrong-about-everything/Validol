package validation.leaf.is.of.value.generic.oneof;

import java.util.List;
import java.util.stream.Collectors;

public class Message<T>
{
    private List<T> list;

    public Message(List<T> list)
    {
        this.list = list;
    }

    public String value()
    {
        return
            String.format(
                "This list must be one of the following: %s.",
                this.list.stream().map(v -> v.toString()).collect(Collectors.joining(", "))
            );
    }
}
