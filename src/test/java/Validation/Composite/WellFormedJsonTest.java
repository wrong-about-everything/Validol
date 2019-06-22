package Validation.Composite;

import Validation.Leaf.*;
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
                    new Named<>("request body", Either.right(this.validJsonRequest()))
                ),
                requestJsonObject ->
                    (new Bloc<>(
                        "request body",
                        List.of(
                            new FastFail<>(
                                new IsMap(
                                    new Required(
                                        new IndexedValue("guest", requestJsonObject)
                                    )
                                ),
                                guestJsonObject ->
                                        (new Bloc<>(
                                            "guest",
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
                                            .result()
                            ),
                            new FastFail<>(
                                new IsArray(
                                    new Required(
                                        new IndexedValue("items", requestJsonObject)
                                    )
                                ),
                                itemsJsonObject ->
                                    (new Bloc<>(
                                        "items",
                                        new Mapped(
                                            itemsJsonObject,
                                            item ->
                                                new UnnamedBlock(
                                                    new IsInteger(
                                                        new Required(
                                                            new IndexedValue("id", item)
                                                        )
                                                    )
                                                )
                                        ),
                                        Items.class
                                    ))
                                        .result()
                            ),
                            new IsInteger(
                                new Required(
                                    new IndexedValue("source", requestJsonObject)
                                )
                            )
                        ),
                        OrderRegistrationRequest.class
                    ))
                        .result()
            ))
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("samokhinvadim@gmail.com", result.value().guest().email());
        assertEquals("Vadim Samokhin", result.value().guest().name());
        assertEquals(Integer.valueOf(1), result.value().source());
    }

    @Test
    public void nonSuccessfulComplexRequest() throws Exception
    {
        Result<OrderRegistrationRequest> result =
            (new FastFail<>(
                new WellFormedJson(
                    new Named<>("parsed request body", Either.right(this.invalidJsonRequest()))
                ),
                requestJsonObject ->
                    (new Bloc<>(
                        "parsed request body",
                        List.of(
                            new FastFail<>(
                                new IsMap(
                                    new Required(
                                        new IndexedValue("guest", requestJsonObject)
                                    )
                                ),
                                guestJsonObject ->
                                    (new Bloc<>(
                                        "guest",
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
                                        // @todo One can get an info about the class name from type parameter.
                                        Guest.class
                                    ))
                                        .result()
                            ),
                            new IsInteger(
                                new Required(
                                    new IndexedValue("source", requestJsonObject)
                                )
                            )
                        ),
                        OrderRegistrationRequest.class
                    ))
                        .result()
            ))
                .result();

        assertFalse(result.isSuccessful());
        assertEquals(
            Map.of(
                "guest",
                Map.of("email", "This one is obligatory"),
                "source",
                "This value must be an integer."
            ),
            result.error()
        );
    }

    @Test
    public void invalidJson() throws Exception
    {
        Result<JsonObject> result =
            (new WellFormedJson(
                new Named<>("vasya", Either.right("invalid json azaza"))
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
                new Named<>("vasya", Either.right(this.validJsonRequest()))
            ))
                .result();

        assertTrue(result.isSuccessful());
        assertEquals(this.validJsonRequest(), result.value().toString());
        assertEquals("{\"name\":\"Vadim Samokhin\",\"email\":\"samokhinvadim@gmail.com\"}", result.value().get("guest").toString());
        assertEquals("Vadim Samokhin", result.value().get("guest").getAsJsonObject().get("name").getAsString());
        assertEquals("samokhinvadim@gmail.com", result.value().get("guest").getAsJsonObject().get("email").getAsString());
    }

    private String validJsonRequest()
    {
        HashMap<String, Object> target = new HashMap<>();
        HashMap<String, Object> inner = new HashMap<>();
        inner.put("email", "samokhinvadim@gmail.com");
        inner.put("name", "Vadim Samokhin");
        target.put("guest", inner);
        target.put("source", 1);

        return new Gson().toJson(target, new TypeToken<HashMap<String, Object>>() {}.getType());
    }

    private String invalidJsonRequest()
    {
        HashMap<String, Object> target = new HashMap<>();
        HashMap<String, Object> inner = new HashMap<>();
        inner.put("name", "Vadim Samokhin");
        target.put("guest", inner);
        target.put("source", "vasya");

        return new Gson().toJson(target, new TypeToken<HashMap<String, Object>>() {}.getType());
    }
}
