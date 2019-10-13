package validation.composite;

import validation.leaf.*;
import validation.result.Result;
import validation.value.Present;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

public class WellFormedJsonTest
{
    @Test
    public void invalidJson() throws Throwable
    {
        Result<JsonElement> result =
            (new WellFormedJson(
                new Named<>("vasya", Either.right(new Present<>("invalid json azaza")))
            ))
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("This must be a valid json", result.error());
    }

    @Test
    public void wellFormedJson() throws Throwable
    {
        Result<JsonElement> result =
            (new WellFormedJson(
                new Named<>("vasya", Either.right(new Present<>(this.validJsonRequest())))
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
