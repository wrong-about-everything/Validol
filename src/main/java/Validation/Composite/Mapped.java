package Validation.Composite;

import Validation.Leaf.AsInteger;
import Validation.Leaf.IndexedValue;
import Validation.Leaf.Named;
import Validation.Result.Result;
import Validation.Result.Unnamed;
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

public class Mapped<T> implements Validatable<List<T>>
{
    private JsonElement jsonElement;
    private Function<JsonElement, UnnamedBloc<T>> innerBlock;

    public Mapped(JsonElement jsonElement, Function<JsonElement, UnnamedBloc<T>> innerBlock)
    {
        this.jsonElement = jsonElement;
        this.innerBlock = innerBlock;
    }

    public Result<List<T>> result() throws Throwable
    {
        Pair<List<Object>, Map<String, Object>> valuesAndErrors =
            new ValuesAndErrors(
                StreamSupport.stream(
                    this.jsonElement.getAsJsonArray().spliterator(),
                    false
                )
                    .map(
                        // TODO: seems that one should pass this function as `Mapped` constructor parameter
                        list -> this.innerBlock.apply(list)
                    )
                    .collect(
                        Collectors.toUnmodifiableList()
                    )
            )
                .value();

        if (valuesAndErrors.getValue1().size() > 0) {
            return
                new Validation.Result.Named<>(
                    "vasya",
                    Either.left(
                        List.of(
                            valuesAndErrors.getValue1()
                        )
                    )
                );
        }

        return
            new Validation.Result.Named<>(
                "vasya",
                Either.right(
                    new Present<>(
                        valuesAndErrors.getValue0().stream()
                            .map(
                                nonCastedObject -> (T) nonCastedObject
                            )
                            .collect(
                                Collectors.toUnmodifiableList()
                            )
                    )
                )
            );
    }
}
