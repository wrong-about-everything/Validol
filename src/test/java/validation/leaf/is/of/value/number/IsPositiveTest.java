package validation.leaf.is.of.value.number;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.leaf.is.of.value.number.IsPositive;
import validation.value.Absent;
import validation.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IsPositiveTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsPositive named =
            new IsPositive(
                new Named<>(
                    "vasya",
                    Either.left("Wooops")
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error());
    }

    @Test
    public void successfulWithAbsentValue() throws Exception
    {
        IsPositive named = new IsPositive(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithPresentZeroValue() throws Exception
    {
        IsPositive named = new IsPositive(new Named<>("vasya", Either.right(new Present<>(0))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
    }

    @Test
    public void successfulWithPresentPositiveValue() throws Exception
    {
        IsPositive named = new IsPositive(new Named<>("vasya", Either.right(new Present<>(2.5))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(2.5, named.result().value().raw());
    }

    @Test
    public void failedWithPresentNegativeValue() throws Exception
    {
        IsPositive named = new IsPositive(new Named<>("vasya", Either.right(new Present<>(-0.05))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be positive.", named.result().error());
    }
}
