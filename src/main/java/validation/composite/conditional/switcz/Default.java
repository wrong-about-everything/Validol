package validation.composite.conditional.switcz;

import validation.result.Result;
import validation.Validatable;

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
