package validation.leaf.is.of.format;

import validation.result.*;
import validation.Validatable;
import com.spencerwi.either.Either;
import validation.value.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;

final public class IsDate implements Validatable<String>
{
    private Validatable<String> original;
    private SimpleDateFormat format;

    public IsDate(Validatable<String> original, SimpleDateFormat format) throws Exception
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
            return new NonSuccessfulWithCustomError<>(prevResult, this.error().getLeft());
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

    private Either<Object, Value<String>> error()
    {
        return Either.left("This value must be a date of a certain format.");
    }
}
