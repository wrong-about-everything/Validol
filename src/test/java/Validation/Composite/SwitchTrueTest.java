package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import Validation.Value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SwitchTrueTest
{
    @Test
    public void success() throws Throwable
    {
        Result<Team> result =
            new SwitchTrue<>(
                "team",
                List.of(
                    new Case<>(
                        () -> true,
                        new UnnamedBlocOfNameds<>(
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
                    )
                )
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("belov", result.value().raw().vasya());
        assertEquals(Integer.valueOf(7), result.value().raw().fedya());
        assertEquals(Map.of("id", 245), result.value().raw().tolya());
        assertEquals(false, result.value().raw().jenya());
    }

    @Test
    public void someFieldsFailed() throws Throwable
    {
        Result<Team> result =
            new SwitchTrue<>(
                "team",
                List.of(
                    new Case<>(
                        () -> true,
                        new UnnamedBlocOfNameds<>(
                            List.of(
                                () -> new Named<>("vasya", Either.right(new Present<>("belov"))),
                                () -> new Named<>("fedya", Either.left("Ooops")),
                                () -> new Named<>("tolya", Either.left("Woooooooops")),
                                () -> new Named<>("jenya", Either.right(new Present<>(false)))
                            ),
                            Team.class
                        )
                    )
                )
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
