package validation.leaf.is.of.type;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.leaf.is.of.type.booolean.IsBoolean;
import validation.result.value.Absent;
import validation.result.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IsBooleanTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsBoolean named =
            new IsBoolean(
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
    public void failedWithIncorrectStructure() throws Exception
    {
        IsBoolean named =
            new IsBoolean(
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
        assertEquals("This value must be a json primitive.", named.result().error());
    }

    @Test
    public void failedWithNonBoolean() throws Exception
    {
        IsBoolean named =
            new IsBoolean(
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
    public void successfulWithPresentValue() throws Exception
    {
        IsBoolean named = new IsBoolean(new Named<>("vasya", Either.right(new Present<>(new JsonPrimitive(false)))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(new JsonPrimitive(false), named.result().value().raw());
    }

    @Test
    public void successfulWithAbsentValue() throws Exception
    {
        IsBoolean named = new IsBoolean(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }
}
