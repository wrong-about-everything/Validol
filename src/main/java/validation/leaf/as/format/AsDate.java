package validation.leaf.as.format;

import validation.Validatable;
import validation.leaf.is.of.format.date.IsDate;
import validation.leaf.is.of.format.date.MustBeValidDate;
import validation.result.*;
import validation.result.error.Error;

import java.text.SimpleDateFormat;
import java.util.Date;

final public class AsDate implements Validatable<Date>
{
    private Validatable<String> original;
    private SimpleDateFormat format;
    private Error error;

    public AsDate(Validatable<String> original, SimpleDateFormat format, Error error) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (format == null) {
            throw new Exception("Format can not be null");
        }
        if (error == null) {
            throw new Exception("error can not be null");
        }

        this.original = original;
        this.format = format;
        this.error = error;
    }

    public AsDate(Validatable<String> original, SimpleDateFormat format) throws Exception
    {
        this(original, format, new MustBeValidDate());
    }

    public Result<Date> result() throws Exception
    {
        Result<String> isDateResult = new IsDate(this.original, this.format, this.error).result();

        if (!isDateResult.isSuccessful()) {
            return new FromNonSuccessful<>(isDateResult);
        }

        if (!isDateResult.value().isPresent()) {
            return new AbsentField<>(isDateResult);
        }

        return new SuccessfulWithCustomValue<>(isDateResult, this.value(isDateResult));
    }

    private Date value(Result<String> prevResult) throws Exception
    {
        this.format.setLenient(false);
        return this.format.parse(prevResult.value().raw().trim());
    }
}
