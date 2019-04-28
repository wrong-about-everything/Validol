package Validation.Result;

import org.junit.Test;
import static org.junit.Assert.*;

class NamedTest
{
    @Test
    public void successful()
    {
        Named<String> result = new Named<>("vasya", "miliy", true);

        assertEquals("vasya", result.name());
        assertEquals("miliy", result.value());
        assertTrue(result.isSuccessful());
    }
}
