package validation.leaf.as.type;

import com.google.gson.Gson;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.is.IndexedValue;
import validation.result.Named;
import validation.result.value.Absent;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

final public class AsBooleanTest
{
    @Test
    public void isBoolean() throws Exception
    {
        AsBoolean named =
            new AsBoolean(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", false)
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals(false, named.result().value().raw());
    }

    @Test
    public void isAlmostBoolean() throws Exception
    {
        AsBoolean named =
            new AsBoolean(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", "true")
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be boolean.", named.result().error().value().get("message"));
    }

    @Test
    public void isNotBooleanAtAll() throws Exception
    {
        AsBoolean named =
            new AsBoolean(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", "hello vasya")
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be boolean.", named.result().error().value().get("message"));
    }

    @Test
    public void isNotAJsonPrimitive() throws Exception
    {
        AsBoolean named =
            new AsBoolean(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", List.of(1, 2, 3))
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be boolean.", named.result().error().value().get("message"));
    }

    @Test
    public void isAbsent() throws Exception
    {
        AsBoolean named =
            new AsBoolean(
                () ->
                    new Named<>(
                        "delivery_by",
                        Either.right(new Absent<>())
                    )
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }
}
