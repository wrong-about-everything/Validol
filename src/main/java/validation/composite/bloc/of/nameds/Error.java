package validation.composite.bloc.of.nameds;

import java.util.Map;

public class Error implements validation.result.error.Error
{
    private Map<String, Object> m;

    public Error(Map<String, Object> m)
    {
        this.m = m;
    }

    @Override
    public Map<String, Object> value()
    {
        return this.m;
    }
}
