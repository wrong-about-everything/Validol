package validation.composite.switcz;

import validation.result.Result;
import validation.Validatable;

final public class Specific<T> implements Case<T>
{
    private Clause clause;
    private Validatable<T> validatable;

    public Specific(Clause clause, Validatable<T> validatable)
    {
        this.clause = clause;
        this.validatable = validatable;
    }

    public Boolean isSatisfied()
    {
        return this.clause.isSatisfied();
    }

    public Result<T> result() throws Throwable
    {
        return this.validatable.result();
    }
}
