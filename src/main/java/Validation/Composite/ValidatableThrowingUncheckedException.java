package Validation.Composite;

import Validation.Result.Result;
import Validation.Validatable;

public class ValidatableThrowingUncheckedException<T> implements Validatable<T>
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}