package Validation.Composite;

import Validation.Result.Result;
import Validation.Result.Unnamed;
import Validation.Validatable;
import Validation.Value.Present;
import Validation.Value.Value;
import com.spencerwi.either.Either;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;

public class UnnamedBloc<T> implements Validatable<T>
{
    private List<Validatable<?>> validatables;
    private final Class<? extends T> clazz;

    public UnnamedBloc(List<Validatable<?>> validatables, Class<? extends T> clazz)
    {
        this.validatables = validatables;
        this.clazz = clazz;
    }

    public Result<T> result() throws Throwable
    {
        // @todo: create non-strictly-type version of NamedBloc, returning Map<String, Object>

        Pair<List<Object>, Map<String, Object>> valuesOrErrors = new ValuesAndErrors(this.validatables).value();

        if (valuesOrErrors.getValue1().size() > 0) {
            return new Unnamed<>(Either.left(valuesOrErrors.getValue1()));
        }

        return
            new Unnamed<>(
                Either.right(
                    new Present<>(
                        this.clazzObjectWithCorrectNumberOfArguments(valuesOrErrors.getValue0().toArray())
                    )
                )
            );
    }

    private T clazzObjectWithCorrectNumberOfArguments(Object[] arguments) throws Exception
    {
        switch (arguments.length) {
            case 0:
                return this.objectWithNoArguments();

            case 1:
                return this.objectWithOneArgument(arguments);

            case 2:
                return this.objectWithTwoArguments(arguments);

            case 3:
                return this.objectWithThreeArguments(arguments);

            default:
                throw new Exception("Fix NamedBloc class to support more T constructor parameters");
        }
    }

    private T objectWithNoArguments() throws Exception
    {
        return
            this.clazz.getDeclaredConstructor(
                this.clazz.getDeclaredConstructors()[0].getParameterTypes()
            )
                .newInstance();
    }

    private T objectWithOneArgument(Object[] arguments) throws Exception
    {
        return
            this.clazz.getDeclaredConstructor(
                this.clazz.getDeclaredConstructors()[0].getParameterTypes()
            )
                .newInstance(
                    ((Value) arguments[0]).raw()
                );
    }

    private T objectWithTwoArguments(Object[] arguments) throws Exception
    {
        return
            this.clazz.getDeclaredConstructor(
                this.clazz.getDeclaredConstructors()[0].getParameterTypes()
            )
                .newInstance(
                    ((Value) arguments[0]).raw(),
                    ((Value) arguments[1]).raw()
                )
            ;
    }

    private T objectWithThreeArguments(Object[] arguments) throws Exception
    {
        return
            this.clazz.getDeclaredConstructor(
                this.clazz.getDeclaredConstructors()[0].getParameterTypes()
            )
                .newInstance(
                    ((Value) arguments[0]).raw(),
                    ((Value) arguments[1]).raw(),
                    ((Value) arguments[2]).raw()
                );
    }
}
