package validation.composite;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.runner.RunWith;
import validation.composite.wellformedjson.WellFormedJson;
import validation.leaf.is.NamedStub;
import validation.result.Result;
import validation.result.value.Present;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
final public class WellFormedJsonTest
{
    @Test
    @UseDataProvider("invalidJsons")
    public void invalidJson(String json) throws Exception
    {
        Result<JsonElement> result =
            (new WellFormedJson(
                new NamedStub<>("vasya", Either.right(new Present<>(json)))
            ))
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("This must be a well-formed json.", result.error().value().get("message"));
    }

    @DataProvider
    public static Object[][] invalidJsons()
    {
        return
            new Object[][] {
                {"{\"hey\": there\"}"},
                {"invalid json azaza\""},
                {"[\"a\": 123]"},
                {"{\"a\":{1, 2}}"},
                {""}
            };
    }

    @Test
    public void wellFormedJson() throws Exception
    {
        Result<JsonElement> result =
            (new WellFormedJson(
                new NamedStub<>("vasya", Either.right(new Present<>(this.validJsonRequest())))
            ))
                .result();

        assertTrue(result.isSuccessful());
        assertEquals(this.validJsonRequest(), result.value().raw().toString());
        assertEquals("{\"name\":\"Vadim Samokhin\",\"email\":\"samokhinvadim@gmail.com\"}", result.value().raw().getAsJsonObject().get("guest").toString());
        assertEquals("Vadim Samokhin", result.value().raw().getAsJsonObject().get("guest").getAsJsonObject().get("name").getAsString());
        assertEquals("samokhinvadim@gmail.com", result.value().raw().getAsJsonObject().get("guest").getAsJsonObject().get("email").getAsString());
    }

    private String validJsonRequest()
    {
        HashMap<String, Object> target = new HashMap<>();
        HashMap<String, Object> inner = new HashMap<>();
        inner.put("email", "samokhinvadim@gmail.com");
        inner.put("name", "Vadim Samokhin");
        target.put("guest", inner);
        target.put(
            "items",
            List.of(
                Map.of("id", 1900),
                Map.of("id", 777)
            )
        );
        target.put("source", 1);

        return new Gson().toJson(target, new TypeToken<HashMap<String, Object>>() {}.getType());
    }
}
