package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.spencerwi.either.Either;

import java.util.function.Function;

public class FastFail<T, R> implements Validatable<R>
{
    private Validatable<T> original;
    private VFunction<T, Result<R>> closure;

    public FastFail(Validatable<T> validatable, VFunction<T, Result<R>> closure)
    {
        this.original = validatable;
        this.closure = closure;
    }

    public Result<R> result() throws Exception
    {
        Result<T> originalResult = this.original.result();

        if (!originalResult.isSuccessful()) {
            return new Named<>(originalResult.name(), Either.left(originalResult.error()));
        }

        return closure.apply(originalResult.value());
    }
}
