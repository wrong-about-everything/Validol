package Validation.Leaf;

import Validation.Result.Result;
import Validation.Result.Named;
import Validation.Validatable;
import com.spencerwi.either.Either;

public class IsInteger<T> implements Validatable<T>
{
    private Validatable<T> original;

    public IsInteger(Validatable<T> original)
    {
        this.original = original;
    }

    public Result<T> result() throws Exception
    {
        Result<T> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return prevResult;
        }

        if (!Integer.valueOf(Integer.parseInt(prevResult.value().toString())).toString().equals(prevResult.value().toString())) {
            return new Named<>(prevResult.name(), Either.left("This value must be an integer."));
        }

        return new Named<>(prevResult.name(), Either.right(prevResult.value()));
    }
}
