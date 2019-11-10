package validation.composite.bloc.of.nameds;

import validation.result.Named;
import validation.result.Result;
import validation.Validatable;
import com.spencerwi.either.Either;
import java.util.List;

final public class NamedBlocOfNameds<T> implements Validatable<T>
{
    private String name;
    private List<Validatable<?>> validatables;
    private final Class<? extends T> clazz;

    public NamedBlocOfNameds(String name, List<Validatable<?>> validatables, Class<? extends T> clazz) throws Exception
    {
        if (name == null) {
            throw new Exception("Name can not be null");
        }
        if (validatables == null) {
            throw new Exception("Validatables list can not be null");
        }
        if (clazz == null) {
            throw new Exception("Clazz can not be null");
        }

        this.name = name;
        this.validatables = validatables;
        this.clazz = clazz;
    }

    public Result<T> result() throws Exception
    {
        Result<T> result = new UnnamedBlocOfNameds<T>(this.validatables, this.clazz).result();

        if (!result.isSuccessful()) {
            return new Named<>(this.name, Either.left(result.error()));
        }

        return new Named<>(this.name, Either.right(result.value()));
    }
}
