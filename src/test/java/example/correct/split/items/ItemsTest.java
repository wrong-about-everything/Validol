package example.correct.split.items;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import example.correct.bag.guest.Guest;
import example.correct.bag.items.Items;
import example.correct.bag.items.item.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.result.Result;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class ItemsTest
{
    @Test
    @UseDataProvider("validRequests")
    public void successfulRequest(JsonElement jsonRequest, Items value) throws Throwable
    {
        Result<Items> result = new example.correct.split.items.Items(jsonRequest).result();

        assertTrue(result.isSuccessful());
        assertEquals(value.list().get(0).id(), result.value().raw().list().get(0).id());
    }

    @DataProvider
    public static Object[][] validRequests() throws Throwable
    {
        return
            new Object[][] {
                {
                    new Gson().toJsonTree(
                        Map.of(
                            "items", List.of(Map.of(
                                "id", 1
                            ))
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    new Items(List.of(new Item(1)))
                }
            };
    }

    @Test
    @UseDataProvider("invalidRequests")
    public void nonSuccessfulRequest(JsonElement jsonRequest, Object errors) throws Throwable
    {
        Result<Items> result = new example.correct.split.items.Items(jsonRequest).result();

        assertFalse(result.isSuccessful());
        assertEquals("items", result.name());
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
                    "This one is obligatory"
                },
                {
                    new Gson().toJsonTree(
                        Map.of(
                            "items", List.of(
                                Map.of(
                                "id", "vasya"
                                ),
                                Map.of(
                                "vasya", "fedya"
                                )
                            )
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    List.of(
                        Map.of("id", "This value must be an integer."),
                        Map.of("id", "This one is obligatory")
                    )
                },
            };
    }
}