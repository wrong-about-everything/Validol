package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.spencerwi.either.Either;

import java.lang.reflect.Constructor;
import java.util.Arrays;
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

        System.out.println(arguments[0]);
        System.out.println(arguments[1]);
        System.out.println(arguments[2]);

        return
            new Named<>(
                "vasya",
                Either.right(
                    this.clazz.getDeclaredConstructor(
                        this.clazz.getDeclaredConstructors()[0].getParameterTypes()
                    )
                        .newInstance(
                            arguments[0], arguments[1], arguments[2]
                        )
                )
            );
    }
}
