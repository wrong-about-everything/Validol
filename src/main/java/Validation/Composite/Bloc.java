package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.spencerwi.either.Either;
import java.util.List;

public class Bloc<T> implements Validatable<T>
{
    private List<Validatable<?>> validatables;
    private final Class<? extends T> clazz;

    public Bloc(List<Validatable<?>> validatables, Class<? extends T> clazz)
    {
        this.validatables = validatables;
        this.clazz = clazz;
    }

    public Result<T> result() throws Exception
    {
        // @todo: think about https://www.baeldung.com/gson-deserialization-guide
        // @todo: validate all validatables!
        // @todo: create non-strictly-type version of Bloc, returning Map<String, Object>

        Object[] arguments =
            this.validatables.stream().map(
                (current) -> {
                    try {
                        return current.result().value();
                    } catch (Exception e) {
                        // @todo Handle this shit: https://www.baeldung.com/java-lambda-exceptions, 3.2
                        throw new RuntimeException();
                    }
                }
            ).toArray();

        return
            new Named<>(
                "vasya",
                Either.right(
                    this.clazzObjectWithCorrectNumberOfArguments(arguments)
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
                throw new Exception("Fix Bloc class to support more T constructor parameters");
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
                .newInstance(arguments[0]);
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
