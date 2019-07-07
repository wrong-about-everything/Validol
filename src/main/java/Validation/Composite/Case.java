package Validation.Composite;

import Validation.Result.Result;
import Validation.Validatable;

interface Case<T>
{
    public Boolean isSatisfied();

    public Result<T> result() throws Throwable;
}
