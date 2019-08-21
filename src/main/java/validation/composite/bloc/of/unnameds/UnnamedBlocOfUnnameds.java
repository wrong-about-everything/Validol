package validation.composite.bloc.of.unnameds;

import validation.result.Result;
import validation.Validatable;
import validation.value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;
import org.javatuples.Pair;
import validation.result.Unnamed;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

final public class UnnamedBlocOfUnnameds<T, R> implements Validatable<R>
{
    private JsonElement jsonElement;
    private Function<JsonElement, Validatable<T>> unnamed;
    private Class<? extends R> clazz;

    public UnnamedBlocOfUnnameds(JsonElement jsonElement, Function<JsonElement, Validatable<T>> unnamed, Class<? extends R> clazz)
    {
        this.jsonElement = jsonElement;
        this.unnamed = unnamed;
        this.clazz = clazz;
    }

    public Result<R> result() throws Throwable
    {
        if (!this.jsonElement.isJsonArray()) {
            return
                new Unnamed<>(
                    Either.left("This block must be an array.")
                );
        }

        Pair<List<T>, List<Object>> valuesAndErrors =
            new ValuesAndErrorsOfUnnameds<>(
                StreamSupport.stream(
                    this.jsonElement.getAsJsonArray().spliterator(),
                    false
                )
                    .map(
                        list -> this.unnamed.apply(list)
                    )
                    .collect(
                        Collectors.toUnmodifiableList()
                    )
            )
                .value();

        if (!valuesAndErrors.getValue1().isEmpty()) {
            return
                new Unnamed<>(
                    Either.left(
                        valuesAndErrors.getValue1()
                    )
                );
        }

        return
            new Unnamed<>(
                Either.right(
                    new Present<>(
                        this.clazz.getDeclaredConstructor(
                            // todoc: There should be a single constructor accepting a single parameter of List type
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
