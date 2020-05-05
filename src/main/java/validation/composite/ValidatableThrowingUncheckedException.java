package validation.composite;

import validation.result.Result;
import validation.Validatable;

final public class ValidatableThrowingUncheckedException<T> implements Validatable<T>
{
    private Validatable<T> original;

    public ValidatableThrowingUncheckedException(Validatable<T> original)
    {
        if (original == null) {
            throw new RuntimeException("Decorated validatable element can not be null");
        }

        this.original = original;
    }

    public Result<T> result() throws RuntimeException
    {
        try {
            return this.original.result();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
