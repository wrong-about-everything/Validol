package validation.composite;

import validation.result.AbsentField;
import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import com.spencerwi.either.Either;

final public class FastFail<T, R> implements Validatable<R>
{
    private Validatable<T> original;
    private VFunction<T, Validatable<R>> closure;

    public FastFail(Validatable<T> validatable, VFunction<T, Validatable<R>> closure) throws Exception
    {
        if (validatable == null) {
            throw new Exception("Validatable can not be null");
        }
        if (closure == null) {
            throw new Exception("Closure can not be null");
        }

        this.original = validatable;
        this.closure = closure;
    }

    public Result<R> result() throws Exception
    {
        Result<T> originalResult = this.original.result();

        if (!originalResult.isSuccessful()) {
            return new Named<>(originalResult.name(), Either.left(originalResult.error()));
        }

        if (!originalResult.value().isPresent()) {
            return new AbsentField<>(originalResult);
        }

        return closure.apply(originalResult.value().raw()).result();
    }
}
