package validation.leaf.is.of.value;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IsPositiveTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
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
    public void failedWithIncorrectStructure() throws Throwable
    {
        IsPositive named =
            new IsPositive(
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
        assertEquals("This value must be a boolean.", named.result().error());
    }

    @Test
    public void failedWithNonBoolean() throws Throwable
    {
        IsPositive named =
            new IsPositive(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Present<>(new JsonPrimitive("vasya"))
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a boolean.", named.result().error());
    }

    @Test
    public void successfulWithAbsentValue() throws Throwable
    {
        IsPositive named = new IsPositive(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithPresentFalseValue() throws Throwable
    {
        IsPositive named = new IsPositive(new Named<>("vasya", Either.right(new Present<>(new JsonPrimitive(false)))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
    }

    @Test
    public void successfulWithPresentTrueValue() throws Throwable
    {
        IsPositive named = new IsPositive(new Named<>("vasya", Either.right(new Present<>(new JsonPrimitive(true)))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(true, named.result().value().raw());
    }
}
