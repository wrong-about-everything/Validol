package validation.result;

import validation.result.value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTest
{
    @Test
    public void successful() throws Exception
    {
        Named<String> result = new Named<>("vasya", Either.right(new Present<>("miliy")));

        assertEquals("vasya", result.name());
        assertEquals("miliy", result.value().raw());
        assertTrue(result.isSuccessful());
    }
}
