package validation.leaf.is.of.format.date;

import validation.result.*;
import validation.Validatable;
import validation.result.error.Error;

import java.text.ParseException;
import java.text.SimpleDateFormat;

final public class IsDate implements Validatable<String>
{
    private Validatable<String> original;
    private SimpleDateFormat format;
    private Error error;

    public IsDate(Validatable<String> original, SimpleDateFormat format, Error error) throws Exception
    {
        if (original == null) {
            throw new Exception("Decorated validatable element can not be null");
        }
        if (format == null) {
            throw new Exception("Format can not be null");
        }
        if (error == null) {
            throw new Exception("Error can not be null");
        }

        this.original = original;
        this.format = format;
        this.error = error;
    }

    public IsDate(Validatable<String> original, SimpleDateFormat format) throws Exception
    {
        this(original, format, new MustBeValidDate());
    }

    public Result<String> result() throws Exception
    {
        Result<String> prevResult = this.original.result();

        if (!prevResult.isSuccessful()) {
            return prevResult;
        }

        if (!prevResult.value().isPresent()) {
            return new AbsentField<>(prevResult);
        }

        if (!this.isValidDate(prevResult)) {
            return new NonSuccessfulWithCustomError<>(prevResult, this.error);
        }

        return prevResult;
    }

    private Boolean isValidDate(Result<String> prevResult) throws Exception
    {
        try {
            this.format.setLenient(false);
            this.format.parse(prevResult.value().raw().trim());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
