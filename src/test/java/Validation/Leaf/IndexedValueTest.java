package Validation.Leaf;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class IndexedValueTest
{
    @Test
    public void nonSuccessful() throws Exception
    {
        IndexedValue named = new IndexedValue("vasya", this.emptyJson());

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Key vasya does not exist", named.result().error());
    }

    @Test
    public void successful() throws Exception
    {
        IndexedValue named = new IndexedValue("vasya", this.json());

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(1, named.result().value().getAsInt());
    }

    private JsonObject emptyJson()
    {
        return
            new JsonParser()
                .parse("{}")
                    .getAsJsonObject();
    }

    private JsonObject json()
    {
        HashMap<String, Object> target = new HashMap<>();
        target.put("vasya", 1);
        target.put("fedya", "vasiliev");
        target.put("tolya", false);
        HashMap<String, Object> inner = new HashMap<>();
        inner.put("lesha", "letyagin");
        target.put("valera", inner);

        return
            new JsonParser()
                .parse(
                    new Gson().toJson(
                        target,
                        new TypeToken<HashMap<String, Object>>() {}.getType()
                    )
                )
                    .getAsJsonObject();
    }
}
