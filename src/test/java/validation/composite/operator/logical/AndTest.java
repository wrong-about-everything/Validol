package validation.composite.operator.logical;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.ErrorStub;
import validation.result.Result;
import validation.result.Unnamed;
import static org.junit.Assert.*;
import validation.result.value.Present;

public class AndTest
{
    @Test
    public void testSuccessful() throws Exception
    {
        assertTrue(
            (new And(
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
        Result<Boolean> result =
            (new And(
                () -> new Unnamed<>(Either.left(new ErrorStub("vasya-vasya..."))),
                () -> new Unnamed<>(Either.right(new Present<>(true)))
            ))
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("vasya-vasya...", result.error().value().get("message"));
    }

    @Test
    public void testSecondOneFailed() throws Exception
    {
        Result<Boolean> result =
            (new And(
                () -> new Unnamed<>(Either.right(new Present<>(true))),
                () -> new Unnamed<>(Either.left(new ErrorStub("fedya-fedya...")))
            ))
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("fedya-fedya...", result.error().value().get("message"));
    }
}
