package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import com.spencerwi.either.Either;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;

// TODO: NamedBlocOfUnnameds to UnnamedBlocOfUnnameds is like NamedBlocOfNameds to UnnamedBlocOfNameds. Implement the former using the latter.
public class UnnamedBlocOfUnnameds<T> implements Validatable<T>
{
    private Validatable<List<?>> validatables;
    private final Class<? extends T> clazz;

    public UnnamedBlocOfUnnameds(Validatable<List<?>> validatables, Class<? extends T> clazz)
    {
        this.validatables = validatables;
        this.clazz = clazz;
    }

    public Result<T> result() throws Throwable
    {
        Pair<List<Object>, List<Map<String, Object>>> valuesAndErrors = new ValuesAndErrorsOfUnnamedBlocs(List.of(this.validatables)).value();

        if (valuesAndErrors.getValue1().size() > 0) {
            return new Named<>(this.name, Either.left(valuesAndErrors.getValue1()));
        }

        return
            new Named<>(
                this.name,
                Either.right(
                    new Present<>(
                        this.clazzObjectWithCorrectNumberOfArguments(valuesAndErrors.getValue0().toArray())
                    )
                )
            );
    }

    // TODO: 6/23/19 Factor out in smth like a Constructor object
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
                throw new Exception("Fix NamedBlocOfNameds class to support more T constructor parameters");
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
                .newInstance(arguments[0])
            ;
    }

    private T objectWithTwoArguments(Object[] arguments) throws Exception
    {
        return
            this.clazz.getDeclaredConstructor(
                this.clazz.getDeclaredConstructors()[0].getParameterTypes()
            )
                .newInstance(arguments[0], arguments[1]);
    }

    private T objectWithThreeArguments(Object[] arguments) throws Exception
    {
        return
            this.clazz.getDeclaredConstructor(
                this.clazz.getDeclaredConstructors()[0].getParameterTypes()
            )
                .newInstance(arguments[0], arguments[1], arguments[2]);
    }
}
