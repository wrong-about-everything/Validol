package validation.composite.operator.logical;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.result.Result;
import validation.result.Unnamed;
import validation.value.Present;

import static org.junit.Assert.*;

public class XorTest
{
    @Test
    public void testSuccessful() throws Throwable
    {
        Result<Boolean> result =
            (new Xor(
                () -> new Unnamed<>(Either.right(new Present<>(true))),
                () -> new Unnamed<>(Either.right(new Present<>(true)))
            ))
                .result();

        assertFalse(result.isSuccessful());
    }

    @Test
    public void testFirstOneFailed() throws Throwable
    {
        assertTrue(
            (new Xor(
                () -> new Unnamed<>(Either.left("vasya-vasya...")),
                () -> new Unnamed<>(Either.right(new Present<>(true)))
            ))
                .result()
                    .isSuccessful()
        );
    }

    @Test
    public void testSecondOneFailed() throws Throwable
    {
        assertTrue(
            (new Xor(
                () -> new Unnamed<>(Either.right(new Present<>(true))),
                () -> new Unnamed<>(Either.left("fedya-fedya..."))
            ))
                .result()
                    .isSuccessful()
        );
    }

    @Test
    public void testBothFailed() throws Throwable
    {
        Result<Boolean> result =
            (new Xor(
                () -> new Unnamed<>(Either.left("vasya-vasya...")),
                () -> new Unnamed<>(Either.left("fedya-fedya..."))
            ))
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("", result.error());
    }
}
