package Validation.Leaf;

import com.spencerwi.either.Either;
import org.junit.Test;

import static org.junit.Assert.*;

public class IsIntegerTest
{
    @Test
    public void nonSuccessful() throws Exception
    {
        IsInteger named = new IsInteger(new Named<String>("vasya", Either.right("miliy")));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be an integer.", named.result().error());
    }

    @Test
    public void successful() throws Exception
    {
        IsInteger named = new IsInteger(new Named<>("vasya", Either.right(666)));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(Integer.valueOf(666), named.result().value());
    }
}
