package Validation.Leaf;

import Validation.Result.Result;
import Validation.Result.Named;
import Validation.Validatable;
import Validation.Value.Present;
import com.spencerwi.either.Either;

public class IsInteger implements Validatable<Integer>
{
    private Validatable<?> original;

    public IsInteger(Validatable<?> original)
    {
        this.original = original;
    }

    public Result<Integer> result() throws Throwable
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
                        // TODO: 6/23/19 Check whether a value is present or not
                        new Present<>(
                            Integer.parseInt(
                                prevResult.value().raw().toString()
                            )
                        )
                    )
                );
        } catch (NumberFormatException e) {
            return new Named<>(prevResult.name(), Either.left("This value must be an integer."));
        }
    }
}
