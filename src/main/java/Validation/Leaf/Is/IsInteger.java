package Validation.Leaf.Is;

import Validation.Result.Result;
import Validation.Result.Named;
import Validation.Result.Unnamed;
import Validation.Validatable;
import Validation.Value.Present;
import Validation.Value.Value;
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
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), Either.left(prevResult.error()))
                    : new Unnamed<>(Either.left(prevResult.error()))
                ;
        }

        try {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), this.value(prevResult))
                    : new Unnamed<>(this.value(prevResult))
                ;
        } catch (NumberFormatException e) {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), this.error())
                    : new Unnamed<>(this.error())
                ;
        }
    }

    private Either<Object, Value<Integer>> value(Result<?> prevResult) throws Throwable
    {
        return
            Either.right(
                // TODO: 6/23/19 Check whether a value is present or not
                new Present<>(
                    Integer.parseInt(prevResult.value().raw().toString())
                )
            );
    }

    private Either<Object, Value<Integer>> error()
    {
        return Either.left("This value must be an integer.");
    }
}
