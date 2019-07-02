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

        assertTrue(result.isSuccessful());
        assertEquals("samokhinvadim@gmail.com", result.value().raw().guest().email());
        assertEquals("Vadim Samokhin", result.value().raw().guest().name());
        assertEquals(1488, result.value().raw().items().list().get(0).id().intValue());
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
//                {
//                    new Gson().toJson(
//                        Map.of(
//                            "guest", Map.of("name", "Vadim Samokhin"),
//                            "source", "vasya"
//                        ),
//                        new TypeToken<HashMap<String, Object>>() {}.getType()
//                    ),
//                    Map.of(
//                        "guest",
//                        Map.of("email", "This one is obligatory"),
//                        "source",
//                        "This value must be an integer.",
//                        "items", "This one is obligatory"
//                    )
//                },
//                {
//                    new Gson().toJson(
//                        Map.of(
//                            "source", "vasya"
//                        ),
//                        new TypeToken<HashMap<String, Object>>() {}.getType()
//                    ),
//                    Map.of(
//                        "guest", "This one is obligatory",
//                        "source", "This value must be an integer.",
//                        "items", "This one is obligatory"
//                    )
//                },
//                {
//                    new Gson().toJson(
//                        Map.of(
//                            "source", 1
//                        ),
//                        new TypeToken<HashMap<String, Object>>() {}.getType()
//                    ),
//                    Map.of(
//                        "guest", "This one is obligatory",
//                        "items", "This one is obligatory"
//                    )
//                },
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
                            "source", 1
                        ),
                        new TypeToken<HashMap<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "items", List.of(Map.of("id", "This should be an integer"))
                    )
                },
            };
    }

    private String validJsonRequest()
    {
        HashMap<String, Object> target = new HashMap<>();
        HashMap<String, Object> inner = new HashMap<>();
        inner.put("email", "samokhinvadim@gmail.com");
        inner.put("name", "Vadim Samokhin");
        target.put("guest", inner);
        target.put("items", List.of(Map.of("id", 1488)));
        target.put("source", 1);

        return new Gson().toJson(target, new TypeToken<HashMap<String, Object>>() {}.getType());
    }
}
