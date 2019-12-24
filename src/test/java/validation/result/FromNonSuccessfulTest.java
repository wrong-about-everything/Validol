package validation.result;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.ErrorStub;

import static org.junit.Assert.*;

final public class FromNonSuccessfulTest
{
    @Test
    public void createdFromNonSuccessfulNamed() throws Exception
    {
        Result<?> result = new FromNonSuccessful<>(new Named<>("vasya", Either.left(new ErrorStub("Oooops"))));

        assertFalse(result.isSuccessful());
        assertEquals("vasya", result.name());
        assertEquals("Oooops", result.error().value().get("message"));
    }

    @Test
    public void createdFromNonSuccessfulUnnamed() throws Exception
    {
        Result<?> result = new FromNonSuccessful<>(new Unnamed<>(Either.left(new ErrorStub("Oooops"))));

        assertFalse(result.isSuccessful());
        assertFalse(result.isNamed());
        assertEquals("Oooops", result.error().value().get("message"));
    }

    @Test
    public void failedToCreateFromSuccessfulUnnamed() throws Exception
    {
        Result<?> result = new FromNonSuccessful<>(new Unnamed<>(Either.left(new ErrorStub("Oooops"))));

        assertFalse(result.isSuccessful());
        assertFalse(result.isNamed());
        assertEquals("Oooops", result.error().value().get("message"));
    }
}
