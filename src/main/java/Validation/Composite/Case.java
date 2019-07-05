package Validation.Composite;

import Validation.Result.Result;
import Validation.Validatable;

// todo: make all classes final
final public class Case<T>
{
    private Clause clause;
    private Validatable<T> validatable;

    public Case(Clause clause, Validatable<T> validatable)
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
