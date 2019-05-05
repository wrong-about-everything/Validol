package Validation.Result;

import com.spencerwi.either.Either;
import org.junit.Test;
import static org.junit.Assert.*;

class NamedTest
{
    @Test
    public void successful() throws Exception
    {
        Named<String> result = new Named<>("vasya", Either.right("miliy"));

        assertEquals("vasya", result.name());
        assertEquals("miliy", result.value());
        assertTrue(result.isSuccessful());
    }
}
