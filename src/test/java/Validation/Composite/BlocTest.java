package Validation.Composite;

import Validation.Result.Named;
import Validation.Result.Result;
import com.spencerwi.either.Either;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class BlocTest
{
    @Test
    public void success() throws Exception {
        Result<Team> result =
            new Bloc<>(
                List.of(
                    () -> new Named<>("vasya", Either.right("belov")),
                    () -> new Named<>("fedya", Either.right(7)),
                    () -> new Named<>("jenya", Either.right(false))
                ),
                Team.class
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("belov", result.value().vasya());
        assertEquals(Integer.valueOf(7), result.value().fedya());
        assertEquals(false, result.value().jenya());
    }

    @Test
    public void someFieldsFailed() throws Exception {
        Result<Team> result =
            new Bloc<>(
                List.of(
                    () -> new Named<>("vasya", Either.right("belov")),
                    () -> new Named<>("fedya", Either.left("Ooops")),
                    () -> new Named<>("jenya", Either.right(false))
                ),
                Team.class
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals(List.of("Ooops"), result.error());
    }
}
