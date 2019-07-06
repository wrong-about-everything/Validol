package Validation.ExampleRequest;

import Validation.Result.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class ExampleJsonRequestTest
{
    @Test
    public void successfulComplexRequest() throws Throwable
    {
        Result<OrderRegistrationRequest> result = new ValidatedOrderRegistrationRequest(this.validJsonRequest()).result();

        // TODO: write test for case when data types declared in validatable don't conform to types in data bags (Item, Delivery),
        // for example when I declare building as `IsInteger`, while corresponding data type in Where block is, say, String.
        // Throw human-readable exception in this case.

        assertTrue(result.isSuccessful());
        assertEquals("samokhinvadim@gmail.com", result.value().raw().guest().email());
        assertEquals("Vadim Samokhin", result.value().raw().guest().name());
        assertEquals(1488, result.value().raw().items().list().get(0).id().intValue());
        assertEquals("Red Square", result.value().raw().delivery().where().street());
        assertEquals(Integer.valueOf(1), result.value().raw().delivery().where().building());
        assertEquals(Integer.valueOf(1), result.value().raw().source());
    }

    @Test
    @UseDataProvider("invalidRequests")
    public void nonSuccessfulComplexRequest(String jsonRequest, Object errors) throws Throwable
    {
        Result<OrderRegistrationRequest> result = new ValidatedOrderRegistrationRequest(jsonRequest).result();

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
                        new TypeToken<HashMap<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "guest",
                        Map.of("email", "This one is obligatory"),
                        "items", "This one is obligatory",
                        "delivery","This one is obligatory",
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
                        new TypeToken<HashMap<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "guest", "This one is obligatory",
                        "items", "This one is obligatory",
                        "delivery", Map.of(
                            "where", Map.of(
                                "street", "This one is obligatory",
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
                        new TypeToken<HashMap<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "guest", "This one is obligatory",
                        "items", "This one is obligatory",
                        "delivery", "This one is obligatory"
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
                        new TypeToken<HashMap<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "items", List.of(Map.of("id", "This should be an integer")),
                        "delivery", Map.of("when", Map.of("date", "This element should represent a date"))
                    )
                },
            };
    }

    private String validJsonRequest()
    {
        return
            new Gson().toJson(
                Map.of(
                    "guest", Map.of(
                        "email", "samokhinvadim@gmail.com",
                        "name", "Vadim Samokhin"
                    ),
                    "items", List.of(Map.of("id", 1488)),
                    "delivery", Map.of(
                        "where", Map.of(
                            "street", "Red Square",
                            "building", 1
                        )
                    ),
                    "source", 1
                ),
                new TypeToken<HashMap<String, Object>>() {}.getType()
            );
    }
}
