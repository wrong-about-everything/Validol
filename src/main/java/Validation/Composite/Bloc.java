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
        // @todo: create non-strictly-type version of Bloc, returning Map<String, Object>


        // @todo: validate all validatables!
        // @todo: replace List of error with Map of errors, so that we output field names

        Stream<Result<?>> results =
            this.validatables.stream()
                .map((current) -> new ValidatableThrowingUncheckedException<>(current))
                .map((current) -> current.result());

        Pair<List<Object>, Map<String, Object>> initialValuesAndErrors = Pair.with(List.of(), Map.of());

        Pair<List<Object>, Map<String, Object>> valuesOrErrors =
                results.reduce(
                        initialValuesAndErrors,
                        // @todo Replace with array_map
                        (currentValuesAndErrors, currentResult) -> {
                            try {
                                if (!currentResult.isSuccessful()) {
                                    Map.Entry<String, Object> result =
                                        Stream.concat(
                                            currentValuesAndErrors.getValue1().entrySet().stream(),
                                            Map.of(currentResult.name(), currentResult.error()).entrySet().stream()
                                        )
                                            .collect(
                                                Collectors.toMap(
                                                    currentMapEntry -> currentMapEntry.getKey(),
                                                    currentMapEntry -> currentMapEntry.getValue()
                                                )
                                            );
                                    return Pair.with(currentValuesAndErrors.getValue0(), newErrors);
                                } else {
                                    List<Object> newValues = new ArrayList<>();
                                    newValues.addAll(currentValuesAndErrors.getValue0());
                                    newValues.addAll(List.of(currentResult.value()));

                                    return Pair.with(newValues, currentValuesAndErrors.getValue1());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        },
                        (accumulativeValuesAndErrors, currentValuesAndErrors) -> {
                            List<Object> values = new ArrayList<>();
                            values.addAll(accumulativeValuesAndErrors.getValue0());
                            values.addAll(currentValuesAndErrors.getValue0());

                            Map<String, Object> errors = Map.of();
                            errors.putAll(accumulativeValuesAndErrors.getValue1());
                            errors.putAll(currentValuesAndErrors.getValue1());

                            return Pair.with(values, errors);
                        }
                );

        if (valuesOrErrors.getValue1().size() > 0) {
            return new Named<>("vasya", Either.left(valuesOrErrors.getValue1()));
        }

        return
            new Named<>(
                "vasya",
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
