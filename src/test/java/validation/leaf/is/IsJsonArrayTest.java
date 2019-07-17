package validation.leaf.is;

import validation.leaf.Named;
import validation.value.Present;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class IsJsonArrayTest
{
    @Test
    public void failedWithJsonPrimitive() throws Throwable
    {
        IsJsonArray named =
            new IsJsonArray(
                new Named<>(
                    "delivery_by",
                    Either.right(
                        new Present<>(
                            new JsonPrimitive("vasya")
                        )
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This element should represent an array", named.result().error());
    }

    @Test
    public void failedWithJsonObject() throws Throwable
    {
        IsJsonArray named =
            new IsJsonArray(
                new Named<>(
                    "json",
                    Either.right(
                        new Present<>(
                            new JsonParser().parse(this.json())
                                .getAsJsonObject()
                        )
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This element should represent an array", named.result().error());
    }

    @Test
    public void successfulWithJsonArray() throws Throwable
    {
        IsJsonArray named =
            new IsJsonArray(
                new Named<>(
                    "delivery_by",
                    Either.right(
                        new Present<>(
                            new JsonParser().parse(this.jsonArray())
                                .getAsJsonArray()
                        )
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("[true,\"vasya\",[]]", named.result().value().raw().toString());
    }

    private String json()
    {
        return
            new Gson().toJson(
                Map.of(
                    "guest", Map.of(
                        "email", "samokhinvadim@gmail.com",
                        "name", "Vadim Samokhin"
                    ),
                    "delivery_by","vasya"
                ),
                new TypeToken<HashMap<String, Object>>() {}.getType()
            );
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
