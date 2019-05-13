package Validation.Leaf;

import Validation.Result.Result;
import Validation.Result.Named;
import Validation.Validatable;
import com.spencerwi.either.Either;

public class IsInteger implements Validatable<Integer>
{
    private Validatable<?> original;

    public IsInteger(Validatable<?> original)
    {
        this.original = original;
    }

    public Result<Integer> result() throws Exception
    {
        Result<?> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new Named<>(prevResult.name(), Either.left(prevResult.error()));
        }

        try {
            return
                new Named<>(
                    prevResult.name(),
                    Either.right(
                        Integer.valueOf(
                            Integer.parseInt(
                                prevResult.value().toString()
                            )
                        )
                    )
                );
        } catch (NumberFormatException e) {
            return new Named<>(prevResult.name(), Either.left("This value must be an integer."));
        }
    }
}
