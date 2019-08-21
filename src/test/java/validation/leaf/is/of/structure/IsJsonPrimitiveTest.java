package validation.leaf.is.of.structure;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.value.Present;

import java.util.HashMap;

import static org.junit.Assert.*;

public class IsJsonPrimitiveTest
{
    @Test
    public void successfulWithJsonPrimitive() throws Throwable
    {
        IsJsonPrimitive named =
            new IsJsonPrimitive(
                new Named<>(
                    "delivery_by",
                    Either.right(
                        new Present<>(
                            new JsonPrimitive("vasya")
                        )
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().value().raw().getAsString());
    }

    @Test
    public void failsWithJsonList() throws Throwable
    {
        IsJsonPrimitive named =
            new IsJsonPrimitive(
                new Named<>(
                    "delivery_by",
                    Either.right(
                        new Present<>(
                            new JsonParser()
                                .parse(this.jsonArray())
                                    .getAsJsonArray()
                        )
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a json primitive", named.result().error());
    }

    @Test
    public void failsWithJsonObject() throws Throwable
    {
        IsJsonPrimitive named =
            new IsJsonPrimitive(
                new Named<>(
                    "jsonObject",
                    Either.right(
                        new Present<>(
                            new JsonParser().parse(this.jsonObject())
                                .getAsJsonObject()
                        )
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a json primitive", named.result().error());
    }

    private String jsonObject()
    {
        HashMap<String, Object> target = new HashMap<>();
        HashMap<String, Object> inner = new HashMap<>();
        inner.put("email", "samokhinvadim@gmail.com");
        inner.put("name", "Vadim Samokhin");
        target.put("guest", inner);
        target.put("delivery_by","vasya");

        return new Gson().toJson(target, new TypeToken<HashMap<String, Object>>() {}.getType());
    }

    private String jsonArray()
    {
        JsonArray jsonArray = new JsonArray(2);
        jsonArray.add(Boolean.TRUE);
        jsonArray.add("vasya");
        jsonArray.add(new JsonArray(0));

        return jsonArray.toString();
    }
}
