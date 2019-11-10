package example.correct.split.source;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import example.correct.bag.items.Items;
import example.correct.bag.items.item.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.result.Result;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class SourceTest
{
    @Test
    @UseDataProvider("validRequests")
    public void successfulRequest(JsonElement jsonRequest, Integer value) throws Exception
    {
        Result<Integer> result = new example.correct.split.source.Source(jsonRequest).result();

        assertTrue(result.isSuccessful());
        assertEquals(value, result.value().raw());
    }

    @DataProvider
    public static Object[][] validRequests() throws Exception
    {
        return
            new Object[][] {
                {
                    new Gson().toJsonTree(
                        Map.of(
                            "source", 1
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    1
                }
            };
    }

    @Test
    @UseDataProvider("invalidRequests")
    public void nonSuccessfulRequest(JsonElement jsonRequest, Object errors) throws Exception
    {
        Result<Integer> result = new example.correct.split.source.Source(jsonRequest).result();

        assertFalse(result.isSuccessful());
        assertEquals("source", result.name());
        assertEquals(errors, result.error());
    }

    @DataProvider
    public static Object[][] invalidRequests()
    {
        return
            new Object[][] {
                {
                    new Gson().toJsonTree(
                        Map.of(),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    "This field is obligatory"
                },
                {
                    new Gson().toJsonTree(
                        Map.of("source", "vasya"),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    "This value must be an integer.",
                },
                {
                    new Gson().toJsonTree(
                        Map.of("source", Map.of("vasya", 1)),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    "This value must be an integer.",
                },
            };
    }
}
