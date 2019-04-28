package Validation;

import Validation.Result.Result;

public interface Validatable<T>
{
    public Result<T> result();
}
