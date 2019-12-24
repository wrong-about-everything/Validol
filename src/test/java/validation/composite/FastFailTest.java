package validation.composite;

import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.result.Result;
import validation.result.value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;

import static org.junit.Assert.*;

final public class FastFailTest
{
    @Test
    public void success() throws Exception
    {
        Result<?> result =
            new FastFail<>(
                () -> new validation.result.Named<>("vasya", Either.right(new Present<>("belov"))),
                belov -> new NamedStub<>("fedya", Either.right(new Present<>("vasiliev")))
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("vasiliev", result.value().raw());
    }

    @Test
    public void fail() throws Exception
    {
        Result<?> result =
            new FastFail<>(
                () ->new validation.result.Named<>("vasya", Either.left(new ErrorStub("Erroneous vasya"))),
                belov -> new NamedStub<>("fedya", Either.right(new Present<>("vasiliev")))
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("Erroneous vasya", result.error().value().get("message"));
    }
}
