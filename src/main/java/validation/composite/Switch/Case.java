package validation.composite.Switch;

import validation.result.Result;

interface Case<T>
{
    public Boolean isSatisfied();

    public Result<T> result() throws Throwable;
}
