package validation.composite.bloc.of.unnameds;

import validation.Validatable;
import org.javatuples.Pair;
import validation.composite.ValidatableThrowingUncheckedException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final public class ValuesAndErrorsOfUnnameds<T>
{
    private List<Validatable<T>> validatables;

    public ValuesAndErrorsOfUnnameds(List<Validatable<T>> validatables)
    {
        this.validatables = validatables;
    }

    public Pair<List<T>, List<Object>> value() throws Throwable
    {
        return
            this.validatables.stream()
                .map((validatable) -> new ValidatableThrowingUncheckedException<>(validatable))
                .map((validatableThrowingUncheckedException) -> validatableThrowingUncheckedException.result())
                .reduce(
                    Pair.with(List.of(), List.of()),
                    (currentValuesAndErrors, currentResult) -> {
                        try {
                            return
                                !currentResult.isSuccessful()
                                    ?
                                    Pair.with(
                                        List.of(),
                                        Stream.concat(
                                            currentValuesAndErrors.getValue1().stream(),
                                            List.of((currentResult.error())).stream()
                                        )
                                            .collect(
                                                Collectors.toUnmodifiableList()
                                            )
                                    )
                                    :
                                    Pair.with(
                                        Stream.concat(
                                            currentValuesAndErrors.getValue0().stream(),
                                            List.of(currentResult.value().raw()).stream()
                                        )
                                            .collect(Collectors.toUnmodifiableList()),
                                        currentValuesAndErrors.getValue1()
                                    )
                                ;
                        } catch (Exception e) {
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
                                accumulativeValuesAndErrors.getValue1().stream(),
                                currentValuesAndErrors.getValue1().stream()
                            )
                                .collect(
                                    Collectors.toUnmodifiableList()
                                )
                        )
                );
    }
}
