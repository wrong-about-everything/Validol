package validation.composite.operator.logical;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.result.Result;
import validation.result.Unnamed;
import validation.result.value.Present;

import static org.junit.Assert.*;

public class OrTest
{
    @Test
    public void testSuccessful() throws Exception
    {
        assertTrue(
            (new Or(
                () -> new Unnamed<>(Either.right(new Present<>(true))),
                () -> new Unnamed<>(Either.right(new Present<>(true)))
            ))
                .result()
                    .isSuccessful()
        );
    }

    @Test
    public void testFirstOneFailed() throws Exception
    {
        assertTrue(
            (new Or(
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
            (new Or(
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
            (new Or(
                () -> new Unnamed<>(Either.left("vasya-vasya...")),
                () -> new Unnamed<>(Either.left("fedya-fedya..."))
            ))
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("Either vasya-vasya... or fedya-fedya...", result.error());
    }
}
