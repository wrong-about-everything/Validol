package Validation.Composite;

import Validation.Leaf.Named;
import Validation.Result.Result;
import Validation.Value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;

import static org.junit.Assert.*;

public class FastFailTest
{
    @Test
    public void success() throws Throwable
    {
        Result<?> result =
            new FastFail<>(
                () -> new Validation.Result.Named<>("vasya", Either.right(new Present<>("belov"))),
                belov -> new Named<>("fedya", Either.right(new Present<>("vasiliev")))
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("vasiliev", result.value().raw());
    }

    @Test
    public void fail() throws Throwable
    {
        Result<?> result =
            new FastFail<>(
                () ->new Validation.Result.Named<>("vasya", Either.left("Erroneous vasya")),
                belov -> new Named<>("fedya", Either.right(new Present<>("vasiliev")))
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("Erroneous vasya", result.error());
    }
}
