package validation.leaf.is.of.value.number;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.leaf.is.of.value.number.IsNegative;
import validation.value.Absent;
import validation.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IsNegativeTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
    {
        IsNegative named =
            new IsNegative(
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
    public void failedWithIncorrectStructure() throws Throwable
    {
        IsNegative named =
            new IsNegative(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Present<>(
                            new Gson().toJsonTree(
                                List.of(
                                    Map.of("id", 1900),
                                    Map.of("id", 777)
                                ),
                                new TypeToken<List<Map<String, Object>>>() {}.getType()
                            )
                        )
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a number.", named.result().error());
    }

    @Test
    public void failedWithNonNumber() throws Throwable
    {
        IsNegative named =
            new IsNegative(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Present<>(new JsonPrimitive("vasya"))
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a number.", named.result().error());
    }

    @Test
    public void successfulWithAbsentValue() throws Throwable
    {
        IsNegative named = new IsNegative(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithPresentZeroValue() throws Throwable
    {
        IsNegative named = new IsNegative(new Named<>("vasya", Either.right(new Present<>(new JsonPrimitive(0)))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be negative.", named.result().error());
    }

    @Test
    public void failedWithPresentPositiveValue() throws Throwable
    {
        IsNegative named = new IsNegative(new Named<>("vasya", Either.right(new Present<>(new JsonPrimitive(0.05)))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be negative.", named.result().error());
    }

    @Test
    public void failedWithPresentNegativeValue() throws Throwable
    {
        IsNegative named = new IsNegative(new Named<>("vasya", Either.right(new Present<>(new JsonPrimitive(-0.05)))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(-0.05, named.result().value().raw());
    }
}
