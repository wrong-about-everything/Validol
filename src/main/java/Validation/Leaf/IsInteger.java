package Validation.Leaf;

import Validation.Result.Result;
import Validation.Result.Named;
import Validation.Validatable;
import com.spencerwi.either.Either;

public class IsInteger<Integer> implements Validatable<Integer>
{
    private Validatable<Integer> original;

    public IsInteger(Validatable<Integer> original)
    {
        this.original = original;
    }

    public Result<Integer> result() throws Exception
    {
        Result<Integer> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return prevResult;
        }

        try {
            java.lang.Integer.parseInt(prevResult.value().toString());
        } catch (NumberFormatException e) {
            return new Named<>(prevResult.name(), Either.left("This value must be an integer."));
        }

        return new Named<>(prevResult.name(), Either.right(prevResult.value()));
    }
}
