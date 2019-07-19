package validation.result;

import com.spencerwi.either.Either;
import org.junit.Test;

import static org.junit.Assert.*;

public class FromNonSuccessfulTest
{
    @Test
    public void createdFromNonSuccessfulNamed() throws Throwable
    {
        Result<?> result = new FromNonSuccessful<>(new Named<>("vasya", Either.left("Oooops")));

        assertFalse(result.isSuccessful());
        assertEquals("vasya", result.name());
        assertEquals("Oooops", result.error());
    }

    @Test
    public void createdFromNonSuccessfulUnnamed() throws Throwable
    {
        Result<?> result = new FromNonSuccessful<>(new Unnamed<>(Either.left("Oooops")));

        assertFalse(result.isSuccessful());
        assertFalse(result.isNamed());
        assertEquals("Oooops", result.error());
    }

    @Test
    public void failedToCreateFromSuccessfulUnnamed() throws Throwable
    {
        Result<?> result = new FromNonSuccessful<>(new Unnamed<>(Either.left("Oooops")));

        assertFalse(result.isSuccessful());
        assertFalse(result.isNamed());
        assertEquals("Oooops", result.error());
    }
}
