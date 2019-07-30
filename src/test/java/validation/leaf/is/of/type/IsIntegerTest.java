package validation.leaf.is.of.type;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;
import com.spencerwi.either.Either;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IsIntegerTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
    {
        IsInteger named =
            new IsInteger(
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
        IsInteger named =
            new IsInteger(
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
        assertEquals("This value must be an integer.", named.result().error());
    }

    @Test
    public void failedWithNonInteger() throws Throwable
    {
        IsInteger named =
            new IsInteger(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Present<>(new JsonPrimitive("vasya"))
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
        IsInteger named = new IsInteger(new Named<>("vasya", Either.right(new Present<>(new JsonPrimitive(777)))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(new JsonPrimitive(777), named.result().value().raw());
    }

    @Test
    public void successfulWithAbsentValue() throws Throwable
    {
        IsInteger named = new IsInteger(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }
}
