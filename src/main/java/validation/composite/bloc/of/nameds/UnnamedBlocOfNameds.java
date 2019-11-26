package validation.composite.bloc.of.nameds;

import validation.result.Result;
import validation.result.Unnamed;
import validation.Validatable;
import validation.result.value.Present;
import com.spencerwi.either.Either;
import org.javatuples.Pair;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

// TODO: use gson.fromJson(reader, Staff.class); instead
final public class UnnamedBlocOfNameds<T> implements Validatable<T>
{
    private List<Validatable<?>> validatables;
    private final Class<? extends T> clazz;

    public UnnamedBlocOfNameds(List<Validatable<?>> validatables, Class<? extends T> clazz) throws Exception
    {
        if (validatables == null) {
            throw new Exception("Validatables can not be null");
        }
        if (clazz == null) {
            throw new Exception("Clazz can not be null");
        }

        this.validatables = validatables;
        this.clazz = clazz;
    }

    public Result<T> result() throws Exception
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
        for (Constructor<?> constructor : this.clazz.getDeclaredConstructors()) {
            try {
                return
                    this.clazz.getDeclaredConstructor(
                        constructor.getParameterTypes()
                    )
                        .newInstance();
            } catch (IllegalArgumentException e) {}
        }

        throw new Exception(
            String.format(
                "You should create an empty constructor in class %s",
                this.clazz.getName()
            )
        );
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
                    "You should create a constructor in class %s with an argument of type %s",
                    this.clazz.getName(),
                    arguments[0].getClass().getName()
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
                    "You should create a constructor in class %s with following arguments in exactly the same order: (%s, %s)",
                    this.clazz.getName(),
                    arguments[0].getClass().getName(),
                    arguments[1].getClass().getName()
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
                    "You should create a constructor in class %s with following arguments in exactly the same order: (%s, %s, %s)",
                    this.clazz.getName(),
                    arguments[0].getClass().getName(),
                    arguments[1].getClass().getName(),
                    arguments[2].getClass().getName()
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
                    "You should create a constructor in class %s with following arguments in exactly the same order: (%s, %s, %s, %s)",
                    this.clazz.getName(),
                    arguments[0].getClass().getName(),
                    arguments[1].getClass().getName(),
                    arguments[2].getClass().getName(),
                    arguments[3].getClass().getName()
                )
            );
    }
}
