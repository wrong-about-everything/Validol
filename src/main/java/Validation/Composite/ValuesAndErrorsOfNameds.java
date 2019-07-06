package Validation.Composite;

import Validation.Validatable;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValuesAndErrorsOfNameds
{
    private List<Validatable<?>> validatables;

    public ValuesAndErrorsOfNameds(List<Validatable<?>> validatables)
    {
        this.validatables = validatables;
    }

    public Pair<List<Object>, Map<String, Object>> value() throws Throwable
    {
        return
            this.validatables.stream()
                .map((validatable) -> new ValidatableThrowingUncheckedException<>(validatable))
                .map((validatableThrowingUncheckedException) -> validatableThrowingUncheckedException.result())
                .reduce(
                    Pair.with(List.of(), Map.of()),
                    (currentValuesAndErrors, currentResult) -> {
                        try {
                            return
                                !currentResult.isSuccessful()
                                    ?
                                    Pair.with(
                                        List.of(),
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
                                            currentResult.value().isPresent()
                                                ? List.of(currentResult.value().raw()).stream()
                                                : List.of().stream()

                                        )
                                            .collect(Collectors.toUnmodifiableList()),
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
    }
}
