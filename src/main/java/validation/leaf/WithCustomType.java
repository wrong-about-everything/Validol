package validation.leaf;

import com.spencerwi.either.Either;
import validation.Validatable;
import validation.result.AbsentField;
import validation.result.FromNonSuccessful;
import validation.result.Named;
import validation.result.Result;
import validation.result.Unnamed;
import validation.value.Present;
import validation.value.Value;

import java.lang.reflect.Constructor;
import java.util.List;

final public class WithCustomType<T, R> implements Validatable<R>
{
    private Validatable<T> validatable;
    private Class<? extends R> clazz;

    public WithCustomType(Validatable<T> validatable, Class<? extends R> clazz)
    {
        this.validatable = validatable;
        this.clazz = clazz;
    }

    @Override
    public Result<R> result() throws Throwable
    {
        Result<T> result = this.validatable.result();

        if (!result.isSuccessful()) {
            return new FromNonSuccessful<>(result);
        }

        if (!result.value().isPresent()) {
            return new AbsentField<>(result);
        }

        return
            result.isNamed()
                ? new Named<R>(result.name(), this.object(result.value().raw()))
                : new Unnamed<R>(this.object(result.value().raw()))
            ;
    }

    private Either<Object, Value<R>> object(T argument) throws Exception
    {
        for (Constructor<?> constructor : this.clazz.getDeclaredConstructors()) {
            try {
                return
                    Either.right(
                        new Present<>(
                            this.clazz.getDeclaredConstructor(
                                constructor.getParameterTypes()
                            )
                                .newInstance(argument)
                        )
                    );
            } catch (IllegalArgumentException e) {}
        }

        throw
            new Exception(
                String.format(
                    "You should create a constructor in class %s with an argument of type %s",
                    this.clazz.getName(),
                    argument.getClass().getName()
                )
            );
    }
}
