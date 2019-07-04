package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import com.spencerwi.either.Either;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;

// TODO: 6/23/19 Create non-strictly-type version of NamedBlocOfNameds, returning Map<String, Object>
public class NamedBlocOfNameds<T> implements Validatable<T>
{
    private String name;
    private List<Validatable<?>> validatables;
    private final Class<? extends T> clazz;

    public NamedBlocOfNameds(String name, List<Validatable<?>> validatables, Class<? extends T> clazz)
    {
        this.name = name;
        this.validatables = validatables;
        this.clazz = clazz;
    }

    public Result<T> result() throws Throwable
    {
        Result<T> result = new UnnamedBlocOfNameds<T>(this.validatables, this.clazz).result();

        if (!result.isSuccessful()) {
            return new Named<>(this.name, Either.left(result.error()));
        }

        return new Named<>(this.name, Either.right(result.value()));
    }
}
