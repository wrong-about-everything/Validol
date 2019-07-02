package Validation.Leaf;

import Validation.Value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnnamedTest
{
    @Test
    public void success() throws Exception
    {
        Unnamed<String> named = new Unnamed<>(Either.right(new Present<>("miliy")));

        assertTrue(named.result().isSuccessful());
        assertEquals("miliy", named.result().value().raw());
    }

    @Test
    public void failure() throws Exception
    {
        Unnamed<String> named = new Unnamed<>(Either.left("ooops"));

        assertFalse(named.result().isSuccessful());
        assertEquals("ooops", named.result().error());
    }
}
