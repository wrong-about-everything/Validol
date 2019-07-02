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

public class NamedBlocOfUnnameds<T> implements Validatable<T>
{
    private String name;
    private Validatable<T> validatable;

    public NamedBlocOfUnnameds(String name, Validatable<T> validatable)
    {
        this.name = name;
        this.validatable = validatable;
    }

    public Result<T> result() throws Throwable
    {
        Result<T> result = this.validatable.result();

        if (!result.isSuccessful()) {
            return new Named<>(this.name, Either.left(result.error()));
        }

        return new Named<>(this.name, Either.right(result.value()));
    }
}
