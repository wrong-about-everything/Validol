package validation.leaf.as;

import com.google.gson.Gson;
import org.junit.Test;
import validation.leaf.IndexedValue;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AsIntegerTest
{
    @Test
    public void isString() throws Throwable
    {
        AsString named =
            new AsString(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", "today")
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("today", named.result().value().raw());
    }

    @Test
    public void isNotAString() throws Throwable
    {
        AsString named =
            new AsString(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", true)
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("true", named.result().value().raw());
    }

    @Test
    public void isNotAJsonPrimitive() throws Throwable
    {
        try {
            new AsString(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", List.of(1, 2, 3))
                    )
                )
            )
                .result();
        } catch (Throwable e) {
            assertEquals("Use IsString validatable to make sure that underlying raw is a string", e.getMessage());
            return;
        }

        fail("An exception should have been thrown");
    }
}
