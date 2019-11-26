package validation.leaf.as.format;

import validation.Validatable;
import validation.leaf.is.of.format.date.IsDate;
import validation.result.*;
import java.text.SimpleDateFormat;
import java.util.Date;

final public class AsDate implements Validatable<Date>
{
    private Validatable<String> original;
    private SimpleDateFormat format;
    private Error customErrorMessage;

//    public AsDate(Validatable<String> original, SimpleDateFormat format, Error customErrorMessage) throws Exception
//    {
//        if (original == null) {
//            throw new Exception("Decorated validatable element can not be null");
//        }
//        if (format == null) {
//            throw new Exception("Format can not be null");
//        }
//        if (customErrorMessage == null) {
//            throw new Exception("customErrorMessage can not be null");
//        }
//
//        this.original = original;
//        this.format = format;
//        this.customErrorMessage = customErrorMessage;
//    }

    public AsDate(Validatable<String> original, SimpleDateFormat format) throws Exception
    {
        this.original = original;
        this.format = format;
//        this(original, format, this.error());
    }

    public Result<Date> result() throws Exception
    {
        Result<String> isDateResult = new IsDate(this.original, this.format/*, this.error()*/).result();

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

//    private error()
//    {
//        return new WrongDateFormat();
//    }
}
