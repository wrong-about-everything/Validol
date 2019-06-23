package Validation.Composite;

import Validation.Result.Result;
import Validation.Result.Unnamed;
import Validation.Validatable;
import Validation.Value.Present;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.spencerwi.either.Either;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Mapped<R> implements Validatable<List<UnnamedBloc<R>>>
{
    private JsonElement jsonElement;
    private Function<JsonElement, Validatable<JsonElement>> returnsValidatable;

    public Mapped(JsonElement jsonElement, Function<JsonElement, Validatable<UnnamedBloc<R>>> returnsValidatable)
    {
        this.jsonElement = jsonElement;
        this.returnsValidatable = returnsValidatable;
    }

    public Result<List<Validatable<E>>> result() throws Exception
    {
        return
            new Unnamed<>(
                Either.right(
                    new Present<>(
                        // TODO: check when it's not an array
                        StreamSupport.stream(this.jsonElement.getAsJsonArray().spliterator(), false)
                            .map(
                                e -> this.returnsValidatable.apply(e)
                            )
                            .collect(
                                Collectors.toUnmodifiableList()
                            )
                    )
                )
            );
    }
}
