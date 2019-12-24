package validation.composite.operator.logical.or;

import validation.result.error.Error;

import java.util.Map;

final public class EitherLeftOrRight implements Error
{
    private String left;
    private String right;

    public EitherLeftOrRight(String left, String right)
    {
        this.left = left;
        this.right = right;
    }

    @Override
    public Map<String, Object> value()
    {
        return
            Map.of(
                "code", new Code().value(),
                "message", new Message(this.left, this.right).value()
            );
    }
}
