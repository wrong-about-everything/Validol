package example.correct.split.guest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import example.correct.bag.Delivery;
import example.correct.bag.delivery.courier.CourierDelivery;
import example.correct.bag.delivery.courier.when.DefaultWhen;
import example.correct.bag.delivery.courier.where.Where;
import example.correct.bag.guest.Guest;
import example.correct.split.delivery.courier.Courier;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.result.Result;

import java.text.SimpleDateFormat;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class GuestTest
{
    @Test
    @UseDataProvider("validRequests")
    public void successfulRequest(JsonElement jsonRequest, Guest value) throws Throwable
    {
        Result<Guest> result = new example.correct.split.guest.Guest(jsonRequest).result();

        assertTrue(result.isSuccessful());
        assertEquals(value.email(), result.value().raw().email());
        assertEquals(value.name(), result.value().raw().name());
    }

    @DataProvider
    public static Object[][] validRequests() throws Throwable
    {
        return
            new Object[][] {
                {
                    new Gson().toJsonTree(
                        Map.of(
                            "guest", Map.of(
                                "email", "vasya@gmail.com",
                                "name", "vasya"
                            )
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    new Guest("vasya@gmail.com", "vasya")
                }
            };
    }

    @Test
    @UseDataProvider("invalidRequests")
    public void nonSuccessfulRequest(JsonElement jsonRequest, Object errors) throws Throwable
    {
        Result<Guest> result = new example.correct.split.guest.Guest(jsonRequest).result();

        assertFalse(result.isSuccessful());
        assertEquals("guest", result.name());
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
                            "guest", Map.of(
                                "email", "vasya@gmail.com"
                            )
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "name", "This one is obligatory"
                    )
                },
                {
                    new Gson().toJsonTree(
                        Map.of(
                            "guest", Map.of(
                                "name", "vasya"
                            )
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "email", "This one is obligatory"
                    )
                },
            };
    }
}
