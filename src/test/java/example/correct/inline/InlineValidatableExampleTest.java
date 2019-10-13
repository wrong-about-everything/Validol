package example.correct.inline;

import example.correct.bag.delivery.courier.CourierDelivery;
import example.correct.bag.delivery.courier.when.DefaultWhen;
import example.correct.bag.delivery.courier.where.Where;
import example.correct.bag.guest.Guest;
import example.correct.bag.items.item.Item;
import example.correct.bag.items.Items;
import example.correct.bag.OrderRegistrationRequestData;
import validation.result.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class InlineValidatableExampleTest
{
    @Test
    @UseDataProvider("validRequests")
    public void successfulComplexRequest(String jsonRequest, OrderRegistrationRequestData value) throws Throwable
    {
        Result<OrderRegistrationRequestData> result = new ValidatedOrderRegistrationRequest(jsonRequest).result();

        assertTrue(result.isSuccessful());
        assertEquals(value.guest().email(), result.value().raw().guest().email());
        assertEquals(value.guest().name(), result.value().raw().guest().name());
        assertEquals(value.items().list().get(0).id().intValue(), result.value().raw().items().list().get(0).id().intValue());
        assertEquals(value.delivery().when().date().isPresent(), result.value().raw().delivery().when().date().isPresent());
        if (value.delivery().when().date().isPresent()) {
            assertEquals(value.delivery().when().date().raw(), result.value().raw().delivery().when().date().raw());
        }
        assertEquals(value.delivery().where().street(), result.value().raw().delivery().where().street());
        assertEquals(value.delivery().where().building(), result.value().raw().delivery().where().building());
        assertEquals(value.source(), result.value().raw().source());
    }

    @DataProvider
    public static Object[][] validRequests() throws Throwable
    {
        return
            new Object[][] {
                {
                    new Gson().toJson(
                        Map.of(
                            "guest", Map.of(
                                "email", "vasya1988@gmail.com",
                                "name", "Vasily Belov"
                            ),
                            "items", List.of(Map.of("id", 1900)),
                            "delivery", Map.of(
                                "where", Map.of(
                                    "street", "Red Square",
                                    "building", 1
                                    ),
                                "when", Map.of(
                                    "date", "2019-07-06 09:52:48"
                                    )
                            ),
                            "source", 1
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    new OrderRegistrationRequestData(
                        new Guest("vasya1988@gmail.com", "Vasily Belov"),
                        new Items(
                            List.of(new Item(1900))
                        ),
                        new CourierDelivery(
                            new Where("Red Square", 1),
                            new DefaultWhen(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-07-06 09:52:48"))
                        ),
                        1
                    )
                },
                {
                    new Gson().toJson(
                        Map.of(
                            "delivery", Map.of(
                                "where", Map.of(
                                    "street", "Red Square",
                                    "building", 1
                                )
                            ),
                            "guest", Map.of(
                                "email", "samokhinvadim@gmail.com",
                                "name", "Vadim Samokhin"
                            ),
                            "items", List.of(Map.of("id", 1900)),
                            "source", 1
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    new OrderRegistrationRequestData(
                        new Guest("samokhinvadim@gmail.com", "Vadim Samokhin"),
                        new Items(
                            List.of(new Item(1900))
                        ),
                        new CourierDelivery(
                            new Where("Red Square", 1)
                        ),
                        1
                    )
                }
            };
    }

    @Test
    @UseDataProvider("invalidRequests")
    public void nonSuccessfulComplexRequest(String jsonRequest, Object errors) throws Throwable
    {
        Result<OrderRegistrationRequestData> result = new ValidatedOrderRegistrationRequest(jsonRequest).result();

        assertFalse(result.isSuccessful());
        assertEquals(errors, result.error());
    }

    @DataProvider
    public static Object[][] invalidRequests()
    {
        return
            new Object[][] {
                {
                    new Gson().toJson(
                        Map.of(
                            "guest", Map.of("name", "Vadim Samokhin"),
                            "source", "vasya"
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "guest",
                        Map.of("email", "This field is obligatory"),
                        "items", "This field is obligatory",
                        "delivery","This field is obligatory",
                        "source","This value must be an integer."
                        )
                },
                {
                    new Gson().toJson(
                        Map.of(
                            "delivery", Map.of(
                                "where", Map.of(
                                    "building", true
                                ),
                                "when", Map.of(
                                    "date", "2019-07-06 09:52:48"
                                )
                            ),
                            "source", "vasya"
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "guest", "This field is obligatory",
                        "items", "This field is obligatory",
                        "delivery", Map.of(
                            "where", Map.of(
                                "street", "This field is obligatory",
                                "building", "This value must be an integer."
                            )
                        ),
                        "source", "This value must be an integer."
                        )
                },
                {
                    new Gson().toJson(
                        Map.of(
                            "source", 1
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "guest", "This field is obligatory",
                        "items", "This field is obligatory",
                        "delivery", "This field is obligatory"
                    )
                },
                {
                    new Gson().toJson(
                        Map.of(
                            "guest", Map.of(
                                    "name", "Vadim Samokhin",
                                    "email", "samokhinvadim@gmail.com"
                            ),
                            "items", List.of(
                                Map.of("id", 'r')
                            ),
                            "delivery",
                                Map.of(
                                    "where", Map.of(
                                            "street", "Res Square",
                                            "building", 2
                                        ),
                                    "when", Map.of(
                                            "date", "vasya"
                                        )
                                ),
                            "source", 1
                        ),
                        new TypeToken<Map<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "items", List.of(Map.of("id", "This value must be an integer.")),
                        "delivery", Map.of("when", Map.of("date", "This value must be a date of a certain format."))
                    )
                },
            };
    }
}
