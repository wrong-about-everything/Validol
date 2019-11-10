package validation.composite.bloc.of.unnameds;

import validation.composite.VFunction;
import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

import java.util.function.Function;

final public class NamedBlocOfUnnameds<T, R> implements Validatable<R>
{
    private String name;
    private JsonElement jsonElement;
    private VFunction<JsonElement, Validatable<T>> unnamed;
    private Class<? extends R> clazz;

    public NamedBlocOfUnnameds(String name, JsonElement jsonElement, VFunction<JsonElement, Validatable<T>> unnamed, Class<? extends R> clazz) throws Exception
    {
        if (name == null) {
            throw new Exception("Name can not be null");
        }
        if (jsonElement == null) {
            throw new Exception("JsonElement can not be null");
        }
        if (unnamed == null) {
            throw new Exception("Unnamed can not be null");
        }
        if (clazz == null) {
            throw new Exception("Clazz can not be null");
        }

        this.name = name;
        this.jsonElement = jsonElement;
        this.unnamed = unnamed;
        this.clazz = clazz;
    }

    public Result<R> result() throws Exception
    {
        Result<R> result = new UnnamedBlocOfUnnameds<T, R>(this.jsonElement, this.unnamed, this.clazz).result();

        if (!result.isSuccessful()) {
            return new Named<>(this.name, Either.left(result.error()));
        }

        return new Named<>(this.name, Either.right(result.value()));
    }
}
