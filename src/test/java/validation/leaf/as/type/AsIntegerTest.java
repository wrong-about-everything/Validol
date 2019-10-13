package validation.leaf.as.type;

import com.google.gson.Gson;
import org.junit.Test;
import validation.leaf.IndexedValue;
import validation.leaf.as.type.AsInteger;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AsIntegerTest
{
    @Test
    public void isInteger() throws Throwable
    {
        AsInteger named =
            new AsInteger(
                new IndexedValue(
                    "quantity",
                    new Gson().toJsonTree(
                        Map.of("quantity", 3)
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals(Integer.valueOf(3), named.result().value().raw());
    }

    @Test
    public void isNotAnInteger() throws Throwable
    {
        AsInteger named =
            new AsInteger(
                new IndexedValue(
                    "quantity",
                    new Gson().toJsonTree(
                        Map.of("quantity", "vasya")
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be an integer.", named.result().error());
    }

    @Test
    public void isNotAJsonPrimitive() throws Throwable
    {
        AsInteger named =
            new AsInteger(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", List.of(1, 2, 3))
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be an integer.", named.result().error());
    }
}
