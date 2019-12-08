package validation.composite.bloc.of.unnameds;

import validation.composite.VFunction;
import validation.composite.bloc.of.unnameds.MustBeAnArray.MustBeAnArray;
import validation.result.Result;
import validation.Validatable;
import validation.result.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import org.javatuples.Pair;
import validation.result.Unnamed;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

final public class UnnamedBlocOfUnnameds<T, R> implements Validatable<R>
{
    private JsonElement jsonElement;
    private VFunction<JsonElement, Validatable<T>> unnamed;
    private Class<? extends R> clazz;

    public UnnamedBlocOfUnnameds(JsonElement jsonElement, VFunction<JsonElement, Validatable<T>> unnamed, Class<? extends R> clazz) throws Exception
    {
        if (jsonElement == null) {
            throw new Exception("JsonElement can not be null");
        }
        if (unnamed == null) {
            throw new Exception("Unnamed can not be null");
        }
        if (clazz == null) {
            throw new Exception("Clazz can not be null");
        }

        this.jsonElement = jsonElement;
        this.unnamed = unnamed;
        this.clazz = clazz;
    }

    public Result<R> result() throws Exception
    {
        if (!this.jsonElement.isJsonArray()) {
            return new Unnamed<>(Either.left(new MustBeAnArray()));
        }

        Pair<List<T>, List<Object>> valuesAndErrors =
            new ValuesAndErrorsOfUnnameds<>(
                StreamSupport.stream(
                    this.jsonElement.getAsJsonArray().spliterator(),
                    false
                )
                    .map(
                        list -> {
                            try {
                                return this.unnamed.apply(list);
                            } catch (Throwable e) {
                                throw new RuntimeException(e);
                            }
                        }
                    )
                    .collect(
                        Collectors.toUnmodifiableList()
                    )
            )
                .value();

        if (!valuesAndErrors.getValue1().isEmpty()) {
            return new Unnamed<>(Either.left(new Error(valuesAndErrors.getValue1())));
        }

        return
            new Unnamed<>(
                Either.right(
                    new Present<>(
                        this.clazz.getDeclaredConstructor(
                            // doc: There should be a single constructor accepting a single parameter of List type
                            this.clazz.getDeclaredConstructors()[0].getParameterTypes()
                        )
                            .newInstance(
                                valuesAndErrors.getValue0().stream()
                                    .collect(
                                        Collectors.toUnmodifiableList()
                                    )
                            )
                    )
                )
            );
    }
}
