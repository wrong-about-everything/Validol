package Validation.Composite;

import Validation.Leaf.Named;
import Validation.Result.Result;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.*;

public class WellFormedJsonTest
{
    @Test
    public void invalidJson() throws Exception
    {
        Result<JsonObject> result =
            (new WellFormedJson(
                new Named<>("vasya", Either.right("invalid json"))
            ))
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("This is an invalid json", result.error());
    }

    @Test
    public void wellFormedJson() throws Exception
    {
        Result<JsonObject> result =
            (new WellFormedJson(
                    new Named<>("vasya", Either.right(this.json()))
            ))
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("{\"fedya\":\"vasiliev\",\"valera\":{\"lesha\":\"letyagin\"},\"vasya\":1,\"tolya\":false}", result.value().toString());
        assertEquals(1, result.value().get("vasya").getAsInt());
        assertEquals("vasiliev", result.value().get("fedya").getAsString());
        assertEquals("{\"lesha\":\"letyagin\"}", result.value().get("valera").toString());
        assertEquals("letyagin", result.value().get("valera").getAsJsonObject().get("lesha").getAsString());
        assertEquals(false, result.value().get("tolya").getAsBoolean());
    }

    private String json()
    {
        HashMap<String, Object> target = new HashMap<>();
        target.put("vasya", 1);
        target.put("fedya", "vasiliev");
        target.put("tolya", false);
        HashMap<String, Object> inner = new HashMap<>();
        inner.put("lesha", "letyagin");
        target.put("valera", inner);

        return new Gson().toJson(target, new TypeToken<HashMap<String, Object>>() {}.getType());
    }
}
