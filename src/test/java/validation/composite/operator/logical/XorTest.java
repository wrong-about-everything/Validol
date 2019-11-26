package validation.composite.operator.logical;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.result.Result;
import validation.result.Unnamed;
import validation.result.value.Present;

import static org.junit.Assert.*;

public class XorTest
{
    @Test
    public void testSuccessful() throws Exception
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
    public void testFirstOneFailed() throws Exception
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
    public void testSecondOneFailed() throws Exception
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
    public void testBothFailed() throws Exception
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
