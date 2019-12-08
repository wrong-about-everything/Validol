package validation.composite.bloc.of.unnameds;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Error implements validation.result.error.Error
{
    private List<Object> m;

    public Error(List<Object> m)
    {
        this.m = m;
    }

    @Override
    public Map<String, Object> value()
    {
        return
            IntStream.range(0, this.m.size())
                .boxed()
                .collect(
                    Collectors.toMap(
                        i -> i.toString(),
                        i -> this.m.get(i)
                    )
                )
            ;
    }
}
