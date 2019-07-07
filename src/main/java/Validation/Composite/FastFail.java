package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Absent;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.spencerwi.either.Either;

import java.util.function.Function;

final public class FastFail<T, R> implements Validatable<R>
{
    private Validatable<T> original;
    private VFunction<T, Validatable<R>> closure;

    public FastFail(Validatable<T> validatable, VFunction<T, Validatable<R>> closure)
    {
        this.original = validatable;
        this.closure = closure;
    }

    public Result<R> result() throws Throwable
    {
        Result<T> originalResult = this.original.result();

        if (!originalResult.isSuccessful()) {
            return new Named<>(originalResult.name(), Either.left(originalResult.error()));
        }

        if (!originalResult.value().isPresent()) {
            return new Named<>(originalResult.name(), Either.right(new Absent<>()));
        }

        return closure.apply(originalResult.value().raw()).result();
    }
}
