package validation.leaf.as.format;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.leaf.is.of.format.IsDate;
import validation.result.*;
import validation.value.Present;
import validation.value.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

final public class AsDate implements Validatable<Date>
{
    private Validatable<String> original;
    private SimpleDateFormat format;

    public AsDate(Validatable<String> original, SimpleDateFormat format) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (format == null) {
            throw new Exception("Format can not be null");
        }

        this.original = original;
        this.format = format;
    }

    public Result<Date> result() throws Throwable
    {
        Result<String> isDateResult = new IsDate(this.original, this.format).result();

        if (!isDateResult.isSuccessful()) {
            return new FromNonSuccessful<>(isDateResult);
        }

        if (!isDateResult.value().isPresent()) {
            return new AbsentField<>(isDateResult);
        }

        return new SuccessfulWithCustomValue<>(isDateResult, this.value(isDateResult));
    }

    private Date value(Result<String> prevResult) throws Throwable
    {
        this.format.setLenient(false);
        return this.format.parse(prevResult.value().raw().trim());
    }
}
