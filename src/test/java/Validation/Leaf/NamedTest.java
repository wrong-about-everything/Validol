package Validation.Leaf;

import Validation.Value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTest
{
    @Test
    public void success() throws Exception
    {
        Named<String> named = new Named<>("vasya", Either.right(new Present<>("miliy")));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("miliy", named.result().value().raw());
    }

    @Test
    public void failure() throws Exception
    {
        Named<String> named = new Named<>("vasya", Either.left("ooops"));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("ooops", named.result().error());
    }
}
