package validation.leaf.as.type;

import com.google.gson.Gson;
import org.junit.Test;
import validation.leaf.is.IndexedValue;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AsStringTest
{
    @Test
    public void isString() throws Exception
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
    public void isNotAString() throws Exception
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

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a string.", named.result().error());
    }

    @Test
    public void isNotAJsonPrimitive() throws Exception
    {
        AsString named =
            new AsString(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", List.of(1, 2, 3))
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a string.", named.result().error());
    }
}
