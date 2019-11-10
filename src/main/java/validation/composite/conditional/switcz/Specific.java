package validation.composite.conditional.switcz;

import validation.result.Result;
import validation.Validatable;

final public class Specific<T> implements Case<T>
{
    private Clause clause;
    private Validatable<T> validatable;

    public Specific(Clause clause, Validatable<T> validatable) throws Exception
    {
        if (clause == null) {
            throw new Exception("Clause can not be null");
        }
        if (validatable == null) {
            throw new Exception("Validatable can not be null");
        }

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
