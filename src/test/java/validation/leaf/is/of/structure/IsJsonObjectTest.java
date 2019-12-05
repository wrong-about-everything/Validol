package validation.leaf.is.of.structure;

import validation.leaf.is.NamedStub;
import validation.leaf.is.of.structure.jsonobject.IsJsonObject;
import validation.result.value.Present;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class IsJsonObjectTest
{
    @Test
    public void failsWithJsonPrimitive() throws Exception
    {
        IsJsonObject named =
            new IsJsonObject(
                new NamedStub<>(
                    "delivery_by",
                    Either.right(
                        new Present<>(
                            new JsonPrimitive("vasya")
                        )
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("This value must be a map", named.result().error());
    }

    @Test
    public void failsWithJsonList() throws Exception
    {
        IsJsonObject named =
            new IsJsonObject(
                new NamedStub<>(
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
        assertEquals("This value must be a map", named.result().error());
    }

    @Test
    public void successfulWithJsonObject() throws Exception
    {
        IsJsonObject named =
            new IsJsonObject(
                new NamedStub<>(
                    "jsonObject",
                    Either.right(
                        new Present<>(
                            new JsonParser().parse(this.jsonObject())
                                .getAsJsonObject()
                        )
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals(this.jsonObject(), named.result().value().raw().toString());
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
