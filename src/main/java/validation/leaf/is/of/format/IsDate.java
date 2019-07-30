package validation.leaf.is.of.format;

import validation.leaf.is.of.structure.IsJsonPrimitive;
import validation.result.*;
import validation.Validatable;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import validation.value.Present;
import validation.value.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;

final public class IsDate implements Validatable<JsonElement>
{
    private Validatable<JsonElement> original;
    private SimpleDateFormat format;

    public IsDate(Validatable<JsonElement> original, SimpleDateFormat format)
    {
        this.original = original;
        this.format = format;
    }

    public Result<JsonElement> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new FromNonSuccessful<>(prevResult);
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!new IsJsonPrimitive(this.original).result().isSuccessful()) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        if (!this.isValidDate(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
        }

        return
            prevResult.isNamed()
                ? new Named<>(prevResult.name(), this.value(prevResult))
                : new Unnamed<>(this.value(prevResult))
            ;
    }

    private Boolean isValidDate(Result<JsonElement> prevResult) throws Throwable
    {
        try {
            this.format.setLenient(false);
            this.format.parse(prevResult.value().raw().getAsString().trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private Either<Object, Value<JsonElement>> value(Result<JsonElement> prevResult) throws Throwable
    {
        return
            Either.right(
                new Present<>(
                    prevResult.value().raw()
                )
            );
    }

    private Either<Object, Value<JsonElement>> error()
    {
        return Either.left("This value must be a date of a certain format.");
    }
}
