package validation.leaf.as;

import com.google.gson.Gson;
import org.junit.Test;
import validation.leaf.IndexedValue;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AsDateTest
{
    @Test
    public void isDate() throws Throwable
    {
        AsDate named =
            new AsDate(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", "2019-05-25 08:07:54")
                    )
                ),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("2019-05-25 08:07:54", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(named.result().value().raw()));
    }

    @Test
    public void isNotADate() throws Throwable
    {
        AsDate named =
            new AsDate(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", "hello vasya")
                    )
                ),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This element should represent a date", named.result().error());
    }

    @Test
    public void isNotAJsonPrimitive() throws Throwable
    {
        try {
            new AsDate(
                new IndexedValue(
                    "delivery_by",
                    new Gson().toJsonTree(
                        Map.of("delivery_by", List.of(1, 2, 3))
                    )
                ),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            )
                .result();
        } catch (Throwable e) {
            assertEquals("Use IsDate validatable to make sure that underlying raw is a date", e.getMessage());
            return;
        }

        fail("An exception should have been thrown");
    }
}
