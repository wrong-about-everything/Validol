package validation.composite.bloc.of.nameds;

import validation.Validatable;
import org.javatuples.Pair;
import validation.composite.ValidatableThrowingUncheckedException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final public class ValuesAndErrorsOfNameds
{
    private List<Validatable<?>> validatables;

    public ValuesAndErrorsOfNameds(List<Validatable<?>> validatables) throws Exception
    {
        if (validatables == null) {
            throw new Exception("Validatables can not be null");
        }

        this.validatables = validatables;
    }

    public Pair<List<Object>, Map<String, Object>> value() throws Exception
    {
        return
            this.validatables.stream()
                .map(
                    (validatable) -> new ValidatableThrowingUncheckedException<>(validatable)
                )
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
                                                Map.of(currentResult.name(), currentResult.error().value()).entrySet().stream()
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
                                            currentValuesAndErrors.getValue1().isEmpty()
                                                ?
                                                    Stream.concat(
                                                        currentValuesAndErrors.getValue0().stream(),
                                                        currentResult.value().isPresent()
                                                            ? List.of(currentResult.value().raw()).stream()
                                                            : List.of().stream()

                                                    )
                                                        .collect(Collectors.toUnmodifiableList())
                                                : List.of(),
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
