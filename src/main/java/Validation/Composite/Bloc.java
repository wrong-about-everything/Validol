package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Validatable;
import com.spencerwi.either.Either;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Bloc<T> implements Validatable<T>
{
    private String name;
    private List<Validatable<?>> validatables;
    private final Class<? extends T> clazz;

    public Bloc(String name, List<Validatable<?>> validatables, Class<? extends T> clazz)
    {
        this.name = name;
        this.validatables = validatables;
        this.clazz = clazz;
    }

    public Result<T> result() throws Exception
    {
        // @todo: create non-strictly-type version of Bloc, returning Map<String, Object>

        Pair<List<Object>, Map<String, Object>> valuesOrErrors =
            this.validatables.stream()
                .map((current) -> new ValidatableThrowingUncheckedException<>(current))
                .map((current) -> current.result())
                .reduce(
                    Pair.with(List.of(), Map.of()),
                    (currentValuesAndErrors, currentResult) -> {
                        try {
                            return
                                !currentResult.isSuccessful()
                                    ?
                                        Pair.with(
                                            currentValuesAndErrors.getValue0(),
                                            Stream.concat(
                                                currentValuesAndErrors.getValue1().entrySet().stream(),
                                                Map.of(currentResult.name(), currentResult.error()).entrySet().stream()
                                            )
                                                .collect(
                                                    Collectors.toMap(
                                                        currentMapEntry -> currentMapEntry.getKey(),
                                                        currentMapEntry -> currentMapEntry.getValue()
                                                    )
                                                )
                                        )
                                    :
                                        Pair.with(
                                            Stream.concat(
                                                currentValuesAndErrors.getValue0().stream(),
                                                List.of(currentResult.value()).stream()
                                            )
                                                .collect(Collectors.toUnmodifiableList())
                                            ,
                                            currentValuesAndErrors.getValue1()
                                        )
                                ;
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    },
                    (accumulativeValuesAndErrors, currentValuesAndErrors) ->
                        Pair.with(
                            Stream.concat(
                                accumulativeValuesAndErrors.getValue0().stream(),
                                currentValuesAndErrors.getValue0().stream()
                            )
                                .collect(
                                    Collectors.toUnmodifiableList()
                                ),
                            Stream.concat(
                                accumulativeValuesAndErrors.getValue1().entrySet().stream(),
                                currentValuesAndErrors.getValue1().entrySet().stream()
                            )
                                .collect(
                                    Collectors.toUnmodifiableMap(
                                        entrySet -> entrySet.getKey(),
                                        entrySet -> entrySet.getValue()
                                    )
                                )
                        )
            );

        if (valuesOrErrors.getValue1().size() > 0) {
            return new Named<>(this.name, Either.left(valuesOrErrors.getValue1()));
        }

        return
            new Named<>(
                this.name,
                Either.right(
                    this.clazzObjectWithCorrectNumberOfArguments(valuesOrErrors.getValue0().toArray())
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
