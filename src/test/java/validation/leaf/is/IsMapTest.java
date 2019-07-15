package validation.leaf.is;

import validation.leaf.Named;
import validation.leaf.is.IsMap;
import validation.value.Present;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class IsMapTest
{
    @Test
    public void jsonPrimitive() throws Throwable
    {
        IsMap named =
            new IsMap(
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
        assertEquals("This element should represent a map", named.result().error());
    }

    @Test
    public void jsonList() throws Throwable
    {
        IsMap named =
            new IsMap(
                new Named<>(
                    "delivery_by",
                    Either.right(
                        new Present<>(
                            new JsonParser()
                                .parse(this.listJson())
                                    .getAsJsonArray()
                        )
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This element should represent a map", named.result().error());
    }

    @Test
    public void successful() throws Throwable
    {
        IsMap named =
            new IsMap(
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

        assertTrue(named.result().isSuccessful());
        assertEquals(this.json(), named.result().value().raw().toString());
    }

    private String json()
    {
        HashMap<String, Object> target = new HashMap<>();
        HashMap<String, Object> inner = new HashMap<>();
        inner.put("email", "samokhinvadim@gmail.com");
        inner.put("name", "Vadim Samokhin");
        target.put("guest", inner);
        target.put("delivery_by","vasya");

        return new Gson().toJson(target, new TypeToken<HashMap<String, Object>>() {}.getType());
    }

    private String listJson()
    {
        JsonArray jsonArray = new JsonArray(2);
        jsonArray.add(Boolean.TRUE);
        jsonArray.add("vasya");
        jsonArray.add(new JsonArray(0));

        return jsonArray.toString();
    }
}
