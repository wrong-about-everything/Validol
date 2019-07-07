package Validation.Leaf.As;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

import java.text.ParseException;
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

        try {
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
        } catch (ParseException e) {
            return new Named<>(prevResult.name(), Either.left("This element should represent a date"));
        }
    }
}
