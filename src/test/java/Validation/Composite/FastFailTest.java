package Validation.Composite;

import Validation.Result.Result;
import com.spencerwi.either.Either;
import org.junit.Test;

import static org.junit.Assert.*;

public class FastFailTest
{
    @Test
    public void success() throws Exception
    {
        Result<?> result =
            (new FastFail<>(
                () -> new Validation.Result.Named<>("vasya", Either.right("belov")),
                belov -> new Validation.Result.Named<>("fedya", Either.right("vasiliev"))
            ))
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("vasiliev", result.value());
    }

    @Test
    public void fail() throws Exception
    {
        Result<?> result =
            (new FastFail<>(
                () ->new Validation.Result.Named<>("vasya", Either.left("Erroneous vasya")),
                belov -> new Validation.Result.Named<>("fedya", Either.right("vasiliev"))
            ))
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("Erroneous vasya", result.error());
    }
}
