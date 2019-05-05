package Validation.Leaf;

import com.spencerwi.either.Either;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IsIntegerTest
{
    @Test
    public void successful() throws Exception
    {
        IsInteger<String> named = new IsInteger<>(new Named<>("vasya", Either.right("miliy")));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("miliy", named.result().value());
    }
}
