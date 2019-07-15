package validation.leaf.as;

import com.google.gson.Gson;
import org.junit.Test;
import validation.leaf.IndexedValue;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AsBooleanTest
{
    @Test
    public void isBoolean() throws Throwable
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
    public void isAlmostBoolean() throws Throwable
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

        assertTrue(named.result().isSuccessful());
        assertEquals(true, named.result().value().raw());
    }

    @Test
    public void isNotBooleanAtAll() throws Throwable
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

        assertTrue(named.result().isSuccessful());
        assertEquals(false, named.result().value().raw());
    }

    @Test
    public void isNotAJsonPrimitive() throws Throwable
    {
        try {
            new AsBoolean(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", List.of(1, 2, 3))
                    )
                )
            )
                .result();
        } catch (Throwable e) {
            assertEquals("Use IsBoolean validatable to make sure that underlying raw is a boolean", e.getMessage());
            return;
        }

        fail("An exception should have been thrown");
    }
}
