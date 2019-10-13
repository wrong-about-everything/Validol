package validation;

import validation.result.Result;

@FunctionalInterface
public interface Validatable<T>
{
    public Result<T> result() throws Throwable;
}
