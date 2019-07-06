package Validation.Leaf.Is;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class IsDate implements Validatable<JsonElement>
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
            return new Named<>(prevResult.name(), Either.left(prevResult.error()));
        }

        try {
            this.format.setLenient(false);
            this.format.parse(prevResult.value().raw().getAsString().trim());
        } catch (ParseException e) {
            return new Named<>(prevResult.name(), Either.left("This element should represent a date"));
        }

        return prevResult;
    }
}
