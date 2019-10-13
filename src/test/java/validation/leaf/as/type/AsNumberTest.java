package validation.leaf.as.type;

import com.google.gson.Gson;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.leaf.IndexedValue;
import validation.leaf.as.type.AsNumber;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class AsNumberTest
{
    @Test
    @UseDataProvider("validNumbers")
    public void isNumber(Number number) throws Throwable
    {
        AsNumber named =
            new AsNumber(
                new IndexedValue(
                    "quantity",
                    new Gson().toJsonTree(
                        Map.of("quantity", number)
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals(number, named.result().value().raw());
    }

    @DataProvider
    public static Object[][] validNumbers()
    {
        return
            new Object[][] {
                {22},
                {-23},
                {2.2},
            };
    }

    @Test
    @UseDataProvider("invalidNumbers")
    public void isNotANumber(Object notANumber) throws Throwable
    {
        AsNumber named =
            new AsNumber(
                new IndexedValue(
                    "quantity",
                    new Gson().toJsonTree(
                        Map.of("quantity", notANumber)
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a number.", named.result().error());
    }

    @DataProvider
    public static Object[][] invalidNumbers()
    {
        return
            new Object[][] {
                {"vasya"},
                {'f'},
                {true},
            };
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
        assertEquals("This value must be a json primitive.", named.result().error());
    }
}
