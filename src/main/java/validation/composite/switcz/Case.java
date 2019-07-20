package validation.composite.switcz;

import validation.result.Result;

interface Case<T>
{
    public Boolean isSatisfied();

    public Result<T> result() throws Throwable;
}
