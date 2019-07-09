package validation.composite;

import validation.result.Result;
import validation.Validatable;

final public class ValidatableThrowingUncheckedException<T> implements Validatable<T>
{
    private Validatable<T> original;

    public ValidatableThrowingUncheckedException(Validatable<T> original)
    {
        this.original = original;
    }

    public Result<T> result() throws RuntimeException
    {
        try {
            return this.original.result();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
