package example.correct.split.delivery.courier;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import example.correct.bag.Delivery;
import example.correct.bag.delivery.courier.CourierDelivery;
import example.correct.bag.delivery.courier.when.DefaultWhen;
import example.correct.bag.delivery.courier.when.EmptyWhen;
import example.correct.bag.delivery.courier.where.EmptyWhere;
import example.correct.bag.delivery.courier.where.Where;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.result.Result;

import java.text.SimpleDateFormat;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class CourierTest
{
    @Test
    @UseDataProvider("validRequests")
    public void successfulRequest(JsonElement jsonRequest, Delivery value) throws Throwable
    {
        Result<Delivery> result = new Courier(jsonRequest).result();

        assertTrue(result.isSuccessful());
        assertEquals(value.when().date().isPresent(), result.value().raw().when().date().isPresent());
        // doc: two ways to deal with empty values. Either with Value monad ...
        if (value.when().date().isPresent()) {
            assertEquals(value.when().date().raw(), result.value().raw().when().date().raw());
        }
        // doc: ... or with isAbsent method on bloc level
        if (!value.where().isAbsent()) {
            assertEquals(value.where().street(), result.value().raw().where().street());
            assertEquals(value.where().building(), result.value().raw().where().building());
        }
    }

    @DataProvider
    public static Object[][] validRequests() throws Throwable
    {
        return
            new Object[][] {
                {
                    new Gson().toJsonTree(
                        Map.of(),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    new CourierDelivery()
                },
                {
                    new Gson().toJsonTree(
                        Map.of(
                            "where", Map.of(
                                "street", "Red Square",
                                "building", 1
                                ),
                            "when", Map.of(
                                "date", "2019-07-06 09:52:48"
                                )
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    new CourierDelivery(
                        new Where("Red Square", 1),
                        new DefaultWhen(
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-07-06 09:52:48")
                        )
                    )
                },
                {
                    new Gson().toJsonTree(
                        Map.of(
                            "where", Map.of(
                                "street", "Red Square",
                                "building", 1
                            )
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    new CourierDelivery(
                        new Where("Red Square", 1)
                    )
                }
            };
    }

    @Test
    @UseDataProvider("invalidRequests")
    public void nonSuccessfulRequest(JsonElement jsonRequest, Object errors) throws Throwable
    {
        Result<Delivery> result = new Courier(jsonRequest).result();

        assertFalse(result.isSuccessful());
        assertEquals(errors, result.error());
    }

    @DataProvider
    public static Object[][] invalidRequests()
    {
        return
            new Object[][] {
                {
                    new Gson().toJsonTree(
                        Map.of(
                            "where", Map.of(
                                "building", true
                            ),
                            "when", Map.of(
                                "date", "2019-07-06 09:52:48"
                            )
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "where", Map.of(
                            "street", "This field is obligatory",
                            "building", "This value must be an integer."
                        )
                    )
                },
                {
                    new Gson().toJsonTree(
                        Map.of(
                            "where", Map.of(
                                    "street", "Res Square",
                                    "building", 2
                                ),
                            "when", Map.of(
                                    "date", "vasya"
                                )
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "when", Map.of(
                            "date", "This value must be a date of a certain format."
                        )
                    )
                },
            };
    }
}
