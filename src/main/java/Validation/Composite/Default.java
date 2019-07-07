package Validation.Composite;

import Validation.Result.Result;
import Validation.Validatable;

final public class Default<T> implements Case<T>
{
    private Validatable<T> validatable;

    public Default(Validatable<T> validatable)
    {
        this.validatable = validatable;
    }

    public Boolean isSatisfied()
    {
        return true;
    }

    public Result<T> result() throws Throwable
    {
        return this.validatable.result();
    }
}
