package validation.leaf.as;

import validation.leaf.is.IsDate;
import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import validation.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import java.text.SimpleDateFormat;
import java.util.Date;

final public class AsDate implements Validatable<Date>
{
    private Validatable<JsonElement> original;
    private SimpleDateFormat format;

    public AsDate(Validatable<JsonElement> original, SimpleDateFormat format)
    {
        this.original = original;
        this.format = format;
    }

    public Result<Date> result() throws Throwable
    {
        Result<JsonElement> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return new Named<>(prevResult.name(), Either.left(prevResult.error()));
        }

        if (!new IsDate(this.original, this.format).result().isSuccessful()) {
            return new Named<>(prevResult.name(), Either.left("This value must be a date of a certain format."));
        }

        this.format.setLenient(false);

        return
            new Named<>(
                prevResult.name(),
                Either.right(
                    new Present<>(
                        this.format.parse(prevResult.value().raw().getAsString().trim())
                    )
                )
            );
    }
}
