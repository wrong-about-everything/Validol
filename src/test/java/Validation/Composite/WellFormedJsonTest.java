package Validation.Composite;

import Validation.Leaf.*;
import Validation.Result.Result;
import Validation.Result.Unnamed;
import Validation.Validatable;
import Validation.Value.Present;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.javatuples.Pair;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

public class WellFormedJsonTest
{
    @Test
    public void successfulComplexRequest() throws Throwable
    {
        Result<OrderRegistrationRequest> result =
            new FastFail<>(
                // Если сделать тут AsMap, то внутри следующих AsMap будет уже не JsonElement, а Map<String, Object>.
                // А AsMap принимает Validatable<JsonElement>!
//                new AsMap(
                    new WellFormedJson(
                        new Named<>("request body", Either.right(new Present<>(this.validJsonRequest())))
                    )
//                )
                ,
                requestJsonMap ->
                    new NamedBloc<>(
                        "request body",
                        List.of(
                            new FastFail<>(
//                                new AsMap(
                                    new IsMap(
                                        new Required(
                                            new IndexedValue("guest", requestJsonMap)
                                        )
                                    )
//                                )
                                ,
                                guestJsonObject ->
                                    new NamedBloc<>(
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
                                    )
                            ),
                            // TODO: move this check into NamedBloc class (a la Block class in Validatable)
                            new FastFail<>(
//                                new AsList(
//                                    new IsArrayOfArrays(
                                        new Required(
                                            new IndexedValue("items", requestJsonMap)
                                        )
//                                    )
//                                )
                                ,
                                itemsJsonElement ->
                                    new NamedBloc<>(
                                    "items",
                                        List.of(
                                            new Mapped<>(
                                                "items",
                                                itemsJsonElement,
                                                item ->
                                                    new UnnamedBloc<>(
                                                        List.of(
                                                            new AsInteger(
                                                                new Required(
                                                                    new IndexedValue("id", item)
                                                                )
                                                            )
                                                        ),
                                                        Item.class
                                                    )
                                            )
                                        ),
                                        Items.class
                                    )
                            ),
                            new IsInteger(
                                new Required(
                                    new IndexedValue("source", requestJsonMap)
                                )
                            )
                        ),
                        OrderRegistrationRequest.class
                    )
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals("samokhinvadim@gmail.com", result.value().raw().guest().email());
        assertEquals("Vadim Samokhin", result.value().raw().guest().name());
        assertEquals(1488, result.value().raw().items().list().get(0).id().intValue());
        assertEquals(666, result.value().raw().items().list().get(1).id().intValue());
        assertEquals(Integer.valueOf(1), result.value().raw().source());
    }

    @Test
    public void nonSuccessfulComplexRequest() throws Throwable
    {
        Result<OrderRegistrationRequest> result =
            new FastFail<>(
                new WellFormedJson(
                    new Named<>("parsed request body", Either.right(new Present<>(this.invalidJsonRequest())))
                ),
                requestJsonObject ->
                    new NamedBloc<>(
                        "parsed request body",
                        List.of(
                            new FastFail<>(
                                new IsMap(
                                    new Required(
                                        new IndexedValue("guest", requestJsonObject)
                                    )
                                ),
                                guestJsonObject ->
                                    new NamedBloc<>(
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
                                    )
                            ),
                            new FastFail<>(
                                new IsArray(
                                    new Required(
                                        new IndexedValue("items", requestJsonObject)
                                    )
                                ),
                                itemsJsonArray ->
                                    new Mapped<>(
                                        "items",
                                        itemsJsonArray,
                                        item ->
                                            new UnnamedBloc<>(
                                                List.of(
                                                    new IsInteger(
                                                        new Required(
                                                            new IndexedValue("id", item)
                                                        )
                                                    )
                                                ),
                                                Item.class
                                            )
                                    )
                            ),
                            new IsInteger(
                                new Required(
                                    new IndexedValue("source", requestJsonObject)
                                )
                            )
                        ),
                        OrderRegistrationRequest.class
                    )
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals(
            Map.of(
                "guest", Map.of("email", "This one is obligatory"),
                "items", List.of(Map.of("id", "This value must be an integer."), Map.of("id", "This value must be an integer.")),
                "source", "This value must be an integer."
            ),
            result.error()
        );
    }

    @Test
    public void invalidJson() throws Throwable
    {
        Result<JsonElement> result =
            (new WellFormedJson(
                new Named<>("vasya", Either.right(new Present<>("invalid json azaza")))
            ))
                .result();

        assertFalse(result.isSuccessful());
        assertEquals("This is an invalid json", result.error());
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
                Map.of("id", 1488),
                Map.of("id", 666)
            )
        );
        target.put("source", 1);

        return new Gson().toJson(target, new TypeToken<HashMap<String, Object>>() {}.getType());
    }

    private String invalidJsonRequest()
    {
        return
            new Gson().toJson(
                Map.of(
                    "guest", Map.of("name", "Vadim Samokhin"),
                    "source", "vasya",
                    "items", List.of(Map.of("id", "vasya"), Map.of("id", false))
                ),
                new TypeToken<HashMap<String, Object>>() {}.getType()
            );
    }
}
