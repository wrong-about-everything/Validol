package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import Validation.Value.Value;
import com.spencerwi.either.Either;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: 6/23/19 Create non-strictly-type version of NamedBloc, returning Map<String, Object>
public class NamedBloc<T> implements Validatable<T>
{
    private String name;
    private List<Validatable<?>> validatables;
    private final Class<? extends T> clazz;

    public NamedBloc(String name, List<Validatable<?>> validatables, Class<? extends T> clazz)
    {
        this.name = name;
        this.validatables = validatables;
        this.clazz = clazz;
    }

    public Result<T> result() throws Throwable
    {
        Pair<List<Object>, Map<String, Object>> valuesAndErrors = new ValuesAndErrors(this.validatables).value();

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
        System.out.println("Clazz is " + this.clazz);
        System.out.println("Argument[0] is " + ((Value) arguments[0]).raw());
        return
            this.clazz.getDeclaredConstructor(
                this.clazz.getDeclaredConstructors()[0].getParameterTypes()
            )
                .newInstance(((Value) arguments[0]).raw())
            ;
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
                );
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
