package validation.composite.bloc.of.named;

import com.spencerwi.either.Either;
import org.javatuples.Pair;
import org.junit.Test;
import validation.composite.bloc.of.nameds.ValuesAndErrorsOfNameds;
import validation.result.Named;
import validation.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ValuesAndErrorsOfNamedsTest
{
    @Test
    public void success() throws Exception
    {
        Pair<List<Object>, Map<String, Object>> result =
            new ValuesAndErrorsOfNameds(
                List.of(
                    () -> new Named<>("vasya", Either.right(new Present<>("belov"))),
                    () -> new Named<>("fedya", Either.right(new Present<>(7))),
                    () -> new Named<>(
                        "tolya",
                        Either.right(
                            new Present<>(
                                Map.of("id", 245)
                            )
                        )
                    ),
                    () -> new Named<>("jenya", Either.right(new Present<>(false)))
                )
            )
                .value();

        assertEquals("belov", result.getValue0().get(0));
        assertEquals(7, result.getValue0().get(1));
        assertEquals(Map.of("id", 245), result.getValue0().get(2));
        assertEquals(false, result.getValue0().get(3));
    }

    @Test
    public void someFieldsFailed() throws Exception
    {
        Pair<List<Object>, Map<String, Object>> result =
            new ValuesAndErrorsOfNameds(
                List.of(
                    () -> new Named<>("vasya", Either.right(new Present<>("belov"))),
                    () -> new Named<>("fedya", Either.left("Ooops")),
                    () -> new Named<>("jenya", Either.right(new Present<>(false))),
                    () -> new Named<>("tolya", Either.left("Woooooooops")),
                    () -> new Named<>("misha", Either.right(new Present<>(true)))
                )
            )
                .value();

        assertEquals(List.of(), result.getValue0());
        assertEquals(
            Map.of(
                "fedya", "Ooops",
                "tolya", "Woooooooops"
            ),
            result.getValue1()
        );
    }
}
