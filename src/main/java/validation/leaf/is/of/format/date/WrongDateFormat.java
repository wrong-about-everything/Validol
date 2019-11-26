package validation.leaf.is.of.format.date;

import validation.result.error.Error;
import java.util.Map;

public class WrongDateFormat implements Error
{
    @Override
    public Map<String, Object> value()
    {
        return
            Map.of(
                "code", new DefaultDateErrorCode().value(),
                "message", new DefaultDateErrorMessage().value()
            );
    }
}
