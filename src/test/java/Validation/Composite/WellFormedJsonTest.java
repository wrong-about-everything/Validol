package Validation.Composite;

import Validation.Leaf.AsString;
import Validation.Leaf.IndexedValue;
import Validation.Leaf.Named;
import Validation.Leaf.Required;
import Validation.Result.Result;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

public class WellFormedJsonTest
{
    @Test
    public void successfulComplexRequest() throws Exception
    {
        Result<OrderRegistrationRequest> result =
            (new FastFail<>(
                new WellFormedJson(
                    new Named<>("request string", Either.right(this.json()))
                ),
                requestJsonObject -> {
                    try {
                        return
                            (new Bloc<>(
                                List.of(
                                    new FastFail<>(
                                        new Required(
                                            new IndexedValue("guest", requestJsonObject)
                                        ),
                                        guestJsonObject -> {
                                            try {
                                                return
                                                    (new Bloc<>(
                                                        List.of(
                                                            new AsString(
                                                                new Required(
                                                                    new IndexedValue("email", guestJsonObject)
                                                                )
                                                            ),
                                                            new AsString(
                                                                new Required(
                                                                    new IndexedValue("name", guestJsonObject)
                                                                )
                                                            )
                                                        ),
                                                        Guest.class
                                                    ))
                                                        .result();
                                            } catch (Exception e) {
                                                throw new RuntimeException();
                                            }
                                        }
                                    )
                                ),
                                OrderRegistrationRequest.class
                            ))
                                .result();
                    } catch (Exception e) {
                        // @todo Handle this shit: https://www.baeldung.com/java-lambda-exceptions, 3.2
                        throw new RuntimeException();
                    }
                }
            ))
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("samokhinvadim@gmail.com", result.value().guest().email());
        assertEquals("Vadim Samokhin", result.value().guest().name());
    }

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
        assertEquals("{\"guest\":{\"name\":\"Vadim Samokhin\",\"email\":\"samokhinvadim@gmail.com\"}}", result.value().toString());
        assertEquals("{\"name\":\"Vadim Samokhin\",\"email\":\"samokhinvadim@gmail.com\"}", result.value().get("guest").toString());
        assertEquals("Vadim Samokhin", result.value().get("guest").getAsJsonObject().get("name").getAsString());
        assertEquals("samokhinvadim@gmail.com", result.value().get("guest").getAsJsonObject().get("email").getAsString());
    }

    private String json()
    {
        HashMap<String, Object> target = new HashMap<>();
        HashMap<String, Object> inner = new HashMap<>();
        inner.put("email", "samokhinvadim@gmail.com");
        inner.put("name", "Vadim Samokhin");
        target.put("guest", inner);

        return new Gson().toJson(target, new TypeToken<HashMap<String, Object>>() {}.getType());
    }
}
