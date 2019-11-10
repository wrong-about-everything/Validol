package validation.composite.conditional.switcz;

import validation.result.Result;

interface Case<T>
{
    public Boolean isSatisfied();

    public Result<T> result() throws Exception;
}
