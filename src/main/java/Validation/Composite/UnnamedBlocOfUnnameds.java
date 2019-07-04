package Validation.Composite;

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

public class UnnamedBlocOfUnnameds<T, R> implements Validatable<R>
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
        Pair<List<Object>, List<Object>> valuesAndErrors =
            new ValuesAndErrorsOfUnnameds(
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
                new Validation.Result.Unnamed<>(
                    Either.left(
                        valuesAndErrors.getValue1()
                    )
                );
        }

        return
            new Validation.Result.Unnamed<>(
                Either.right(
                    new Present<>(
                        this.clazz.getDeclaredConstructor(
                            this.clazz.getDeclaredConstructors()[0].getParameterTypes()
                        )
                            .newInstance(
                                valuesAndErrors.getValue0().stream()
                                    .map(
                                        nonCastedObject -> (T) nonCastedObject
                                    )
                                    .collect(
                                        Collectors.toUnmodifiableList()
                                    )
                            )
                    )
                )
            );
    }
}
