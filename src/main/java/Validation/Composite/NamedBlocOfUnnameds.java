package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

final public class NamedBlocOfUnnameds<T, R> implements Validatable<R>
{
    private String name;
    private JsonElement jsonElement;
    private Function<JsonElement, Validatable<T>> unnamed;
    private Class<? extends R> clazz;

    public NamedBlocOfUnnameds(String name, JsonElement jsonElement, Function<JsonElement, Validatable<T>> unnamed, Class<? extends R> clazz)
    {
        this.name = name;
        this.jsonElement = jsonElement;
        this.unnamed = unnamed;
        this.clazz = clazz;
    }

    public Result<R> result() throws Throwable
    {
        Result<R> result = new UnnamedBlocOfUnnameds<T, R>(this.jsonElement, this.unnamed, this.clazz).result();

        if (!result.isSuccessful()) {
            return new Named<>(this.name, Either.left(result.error()));
        }

        return new Named<>(this.name, Either.right(result.value()));
    }
}
