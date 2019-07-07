package Validation.Composite;

import Validation.Result.Result;
import Validation.Result.Unnamed;
import Validation.Validatable;
import Validation.Value.Present;
import com.spencerwi.either.Either;
import org.javatuples.Pair;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

final public class UnnamedBlocOfNameds<T> implements Validatable<T>
{
    private List<Validatable<?>> validatables;
    private final Class<? extends T> clazz;

    public UnnamedBlocOfNameds(List<Validatable<?>> validatables, Class<? extends T> clazz)
    {
        this.validatables = validatables;
        this.clazz = clazz;
    }

    public Result<T> result() throws Throwable
    {
        Pair<List<Object>, Map<String, Object>> valuesOrErrors = new ValuesAndErrorsOfNameds(this.validatables).value();

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

            case 4:
                return this.objectWithFourArguments(arguments);

            default:
                throw new Exception("Fix UnnamedBlocOfNameds class to support more T constructor parameters");
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
        for (Constructor<?> constructor : this.clazz.getDeclaredConstructors()) {
            try {
                return
                    this.clazz.getDeclaredConstructor(
                        constructor.getParameterTypes()
                    )
                        .newInstance(arguments[0]);
            } catch (IllegalArgumentException e) {}
        }

        throw
            new Exception(
                String.format(
                    "You should create a constructor with a following argument: %s",
                    arguments[0].getClass()
                )
            );
    }

    private T objectWithTwoArguments(Object[] arguments) throws Exception
    {
        for (Constructor<?> constructor : this.clazz.getDeclaredConstructors()) {
            try {
                return
                    this.clazz.getDeclaredConstructor(
                        constructor.getParameterTypes()
                    )
                        .newInstance(arguments[0], arguments[1]);
            } catch (IllegalArgumentException e) {}
        }

        throw
            new Exception(
                String.format(
                    "You should create a constructor with following arguments: %s, %s",
                    arguments[0].getClass(),
                    arguments[1].getClass()
                )
            );
    }

    private T objectWithThreeArguments(Object[] arguments) throws Exception
    {
        for (Constructor<?> constructor : this.clazz.getDeclaredConstructors()) {
            try {
                return
                    this.clazz.getDeclaredConstructor(
                        constructor.getParameterTypes()
                    )
                        .newInstance(arguments[0], arguments[1], arguments[2]);
            } catch (IllegalArgumentException e) {}
        }

        throw
            new Exception(
                String.format(
                    "You should create a constructor with following arguments: %s, %s, %s",
                    arguments[0].getClass(),
                    arguments[1].getClass(),
                    arguments[2].getClass()
                )
            );
    }

    private T objectWithFourArguments(Object[] arguments) throws Exception
    {
        for (Constructor<?> constructor : this.clazz.getDeclaredConstructors()) {
            try {
                return
                    this.clazz.getDeclaredConstructor(
                        constructor.getParameterTypes()
                    )
                        .newInstance(arguments[0], arguments[1], arguments[2], arguments[3]);
            } catch (IllegalArgumentException e) {}
        }

        throw
            new Exception(
                String.format(
                    "You should create a constructor with following arguments: %s, %s, %s",
                    arguments[0].getClass(),
                    arguments[1].getClass(),
                    arguments[2].getClass(),
                    arguments[3].getClass()
                )
            );
    }
}
