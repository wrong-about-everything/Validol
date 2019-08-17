package validation.leaf.as;

import com.google.gson.Gson;
import org.junit.Test;
import validation.leaf.IndexedValue;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AsNumberTest
{
    @Test
    public void isNumber() throws Throwable
    {
        AsNumber named =
            new AsNumber(
                new IndexedValue(
                    "quantity",
                    new Gson().toJsonTree(
                        Map.of("quantity", 3)
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals(Number.valueOf(3), named.result().value().raw());
    }

    @Test
    public void isNotAnNumber() throws Throwable
    {
        AsNumber named =
            new AsNumber(
                new IndexedValue(
                    "quantity",
                    new Gson().toJsonTree(
                        Map.of("quantity", "vasya")
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a number.", named.result().error());
    }

    @Test
    public void isNotAJsonPrimitive() throws Throwable
    {
        AsNumber named =
            new AsNumber(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", List.of(1, 2, 3))
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be an Number.", named.result().error());
    }
}
