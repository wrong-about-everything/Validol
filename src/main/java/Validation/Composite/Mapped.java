package Validation.Composite;

import Validation.Result.Result;
import Validation.Result.Unnamed;
import Validation.Validatable;
import Validation.Value.Present;
import com.google.gson.JsonElement;
import com.spencerwi.either.Either;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Mapped<UNNAMED_BLOC_RESULT_TYPE> implements Validatable<List<UnnamedBloc<UNNAMED_BLOC_RESULT_TYPE>>>
{
    private JsonElement jsonElement;
    private Function<JsonElement, UnnamedBloc<UNNAMED_BLOC_RESULT_TYPE>> returnsValidatable;

    public Mapped(JsonElement jsonElement, Function<JsonElement, UnnamedBloc<UNNAMED_BLOC_RESULT_TYPE>> returnsValidatable)
    {
        this.jsonElement = jsonElement;
        this.returnsValidatable = returnsValidatable;
    }

    public Result<List<UnnamedBloc<UNNAMED_BLOC_RESULT_TYPE>>> result() throws Exception
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
