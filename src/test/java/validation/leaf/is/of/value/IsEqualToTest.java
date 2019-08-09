package validation.leaf.is.of.value;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.leaf.is.of.value.IsEqualTo;
import validation.value.Absent;
import validation.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IsEqualToTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
    {
        IsEqualTo named =
            new IsEqualTo(
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
        IsEqualTo named =
            new IsEqualTo(
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
        IsEqualTo named =
            new IsEqualTo(
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
        IsEqualTo named = new IsEqualTo(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void successfulWithPresentFalseValue() throws Throwable
    {
        IsEqualTo named = new IsEqualTo(new Named<>("vasya", Either.right(new Present<>(new JsonPrimitive(false)))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(false, named.result().value().raw());
    }

    @Test
    public void failedWithPresentTrueValue() throws Throwable
    {
        IsEqualTo named = new IsEqualTo(new Named<>("vasya", Either.right(new Present<>(new JsonPrimitive(true)))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
    }
}
