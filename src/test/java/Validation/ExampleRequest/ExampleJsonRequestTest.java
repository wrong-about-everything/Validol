package Validation.ExampleRequest;

import Validation.Composite.*;
import Validation.Leaf.*;
import Validation.Result.Result;
import Validation.Validatable;
import Validation.Value.Present;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.javatuples.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
//import com.tngtech.java.junit.dataprovider.DataProvider;
//import com.tngtech.java.junit.dataprovider.DataProviderRunner;
//import com.tngtech.java.junit.dataprovider.UseDataProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class ExampleJsonRequestTest
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
                            new FastFail<JsonElement, Items>(
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
                                                // TODO: Extract this fucntionality to Mapped class, add list iteration there
                                            new Validatable<List<Item>>() {
                                                @Override
                                                public Result<List<Item>> result() throws Throwable
                                                {
                                                    Pair<List<Object>, Map<String, Object>> valuesAndErrors =
                                                        new ValuesAndErrors(
                                                            List.of(
                                                                    // TODO: form UnnamedBlocs dynamically, as many as there are in json
                                                                new UnnamedBloc<>(
                                                                    List.of(
                                                                        new Named<>("id", Either.right(new Present<>(1488)))
                                                                    ),
                                                                    Item.class
                                                                )
                                                            )
                                                        )
                                                            .value();

                                                    if (valuesAndErrors.getValue1().size() > 0) {
                                                        return
                                                            new Validation.Result.Named<>(
                                                                "vasya",
                                                                Either.left(
                                                                    List.of(
                                                                        valuesAndErrors.getValue1()
                                                                    )
                                                                )
                                                            );
                                                    }

                                                    return
                                                        new Validation.Result.Named<>(
                                                            "vasya",
                                                            Either.right(
                                                                new Present<>(
                                                                    List.of(
                                                                        // TODO: pass through values and cast them  to Item
                                                                        (Item) valuesAndErrors.getValue0().get(0)
                                                                    )
                                                                )
                                                            )
                                                        );
                                                }
                                            }
                                        )
//                                        new Mapped<Item>(
//                                            itemsJsonElement,
//                                            item ->
//                                                new UnnamedBloc<>(
//                                                    List.of(
//                                                        new IsInteger(
//                                                            new Required(
//                                                                new IndexedValue("id", item)
//                                                            )
//                                                        )
//                                                    ),
//                                                    Item.class
//                                                )
//                                        )
//                                            .result()
//                                            .value()
//                                            .raw()
                                            ,
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
        assertEquals(Integer.valueOf(1), result.value().raw().source());
    }

    @Test
    @UseDataProvider("invalidRequests")
    public void nonSuccessfulComplexRequest(String jsonRequest, Object errors) throws Throwable
    {
        Result<OrderRegistrationRequest> result = new ValidatedOrderRegistrationRequest(jsonRequest).result();

        assertFalse(result.isSuccessful());
        assertEquals(
            errors,
            result.error()
        );
    }

    @DataProvider
    public static Object[][] invalidRequests()
    {
        return
            new Object[][] {
                {
                    new Gson().toJson(
                        Map.of(
                            "guest", Map.of("name", "Vadim Samokhin"),
                            "source", "vasya"
                        ),
                        new TypeToken<HashMap<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "guest",
                        Map.of("email", "This one is obligatory"),
                        "source",
                        "This value must be an integer."
                    )
                },
                {
                    new Gson().toJson(
                        Map.of(
                            "source", "vasya"
                        ),
                        new TypeToken<HashMap<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "guest", "This one is obligatory",
                        "source",
                        "This value must be an integer."
                    )
                },
                {
                    new Gson().toJson(
                        Map.of(
                            "source", 1
                        ),
                        new TypeToken<HashMap<String, Object>>() {}.getType()
                    ),
                    Map.of(
                        "guest", "This one is obligatory"
                    )
                },
            };
    }

    private String validJsonRequest()
    {
        HashMap<String, Object> target = new HashMap<>();
        HashMap<String, Object> inner = new HashMap<>();
        inner.put("email", "samokhinvadim@gmail.com");
        inner.put("name", "Vadim Samokhin");
        target.put("guest", inner);
        target.put("items", List.of(Map.of("id", 1488)));
        target.put("source", 1);

        return new Gson().toJson(target, new TypeToken<HashMap<String, Object>>() {}.getType());
    }
}
