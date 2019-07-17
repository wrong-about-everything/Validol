package validation.leaf.is;

import com.google.gson.JsonElement;
import validation.result.Result;
import validation.result.Named;
import validation.result.Unnamed;
import validation.Validatable;
import validation.value.Absent;
import validation.value.Present;
import validation.value.Value;
import com.spencerwi.either.Either;

// todo: refactor a la IsString
final public class IsInteger implements Validatable<Integer>
{
    private Validatable<JsonElement> original;

    public IsInteger(Validatable<JsonElement> original)
    {
        this.original = original;
    }

    public Result<Integer> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), Either.left(prevResult.error()))
                    : new Unnamed<>(Either.left(prevResult.error()))
                ;
        }

        if (!prevResult.value().isPresent()) {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), Either.right(new Absent<>()))
                    : new Unnamed<>(Either.right(new Absent<>()))
                ;
        }

        if (!new IsJsonPrimitive(this.original).result().isSuccessful()) {
            return
                prevResult.isNamed()
                    ? new Named<>(prevResult.name(), this.error())
                    : new Unnamed<>(this.error())
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
        if (!prevResult.value().isPresent()) {
            return Either.right(new Absent<>());
        }

        return
            Either.right(
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
