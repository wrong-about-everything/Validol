package validation.composite.bloc.of.named;

import validation.composite.bloc.of.nameds.NamedBlocOfNameds;
import validation.result.Named;
import validation.result.Result;
import validation.result.value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class NamedBlocOfNamedsTest
{
    @Test
    public void success() throws Exception
    {
        Result<Team> result =
            new NamedBlocOfNameds<>(
                "team",
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
                ),
                Team.class
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("belov", result.value().raw().vasya());
        assertEquals(Integer.valueOf(7), result.value().raw().fedya());
        assertEquals(Map.of("id", 245), result.value().raw().tolya());
        assertEquals(false, result.value().raw().jenya());
    }

    @Test
    public void someFieldsFailed() throws Exception
    {
        Result<Team> result =
            new NamedBlocOfNameds<>(
                "team",
                List.of(
                    () -> new Named<>("vasya", Either.right(new Present<>("belov"))),
                    () -> new Named<>("fedya", Either.left("Ooops")),
                    () -> new Named<>("tolya", Either.left("Woooooooops")),
                    () -> new Named<>("jenya", Either.right(new Present<>(false)))
                ),
                Team.class
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals(
            Map.of(
                "fedya", "Ooops",
                "tolya", "Woooooooops"
            ),
            result.error()
        );
    }
}
