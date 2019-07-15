package validation.leaf.is;

import validation.leaf.Named;
import validation.leaf.is.IsInteger;
import validation.value.Absent;
import validation.value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;

import static org.junit.Assert.*;

public class IsIntegerTest
{
    @Test
    public void nonSuccessful() throws Throwable
    {
        IsInteger named =
            new IsInteger(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Present<>("miliy")
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be an integer.", named.result().error());
    }

    @Test
    public void successfulWithPresentValue() throws Throwable
    {
        IsInteger named = new IsInteger(new Named<>("vasya", Either.right(new Present<>(666))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(Integer.valueOf(666), named.result().value().raw());
    }

    @Test
    public void successfulWithAbsentValue() throws Throwable
    {
        IsInteger named = new IsInteger(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }
}
