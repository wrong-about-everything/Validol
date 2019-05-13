package Validation;

import Validation.Result.Result;

@FunctionalInterface
public interface Validatable<T>
{
    public Result<T> result() throws Exception;
}
