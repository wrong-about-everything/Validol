package validation.leaf;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import validation.leaf.is.IndexedValue;

import java.util.HashMap;

import static org.junit.Assert.*;

final public class IndexedValueTest
{
    @Test
    public void nonSuccessful() throws Exception
    {
        IndexedValue named = new IndexedValue("vasya", this.emptyJson());

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void successfulNonNull() throws Exception
    {
        IndexedValue vasya = new IndexedValue("vasya", this.json());

        assertTrue(vasya.result().isSuccessful());
        assertEquals("vasya", vasya.result().name());
        assertEquals(1, vasya.result().value().raw().getAsInt());
    }

    @Test
    public void successfulNull() throws Exception
    {
        IndexedValue fedya = new IndexedValue("fedya", this.json());

        assertTrue(fedya.result().isSuccessful());
        assertFalse(fedya.result().value().isPresent());
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
        target.put("fedya", null);
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
