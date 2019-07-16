package validation.leaf.is;

import com.google.gson.JsonPrimitive;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;

import static org.junit.Assert.*;

public class IsStringTest
{
    @Test
    public void successfulWithPresentValue() throws Throwable
    {
        IsString named =
            new IsString(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Present<>(new JsonPrimitive("belov"))
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("belov", named.result().value().raw());
    }

    @Test
    public void successfulWithAbsentValue() throws Throwable
    {
        IsString named = new IsString(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void nonSuccessful() throws Throwable
    {
        IsString named = new IsString(new Named<>("vasya", Either.right(new Present<>(new JsonPrimitive(666)))));

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a string.", named.result().error());
    }
}
