package Validation.Leaf;

import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTest
{
    @Test
    public void successful()
    {
        Named<String> named = new Named<>("vasya", "miliy", true);

        assertEquals("vasya", named.result().name());
        assertEquals("miliy", named.result().value());
        assertTrue(named.result().isSuccessful());
    }
}
