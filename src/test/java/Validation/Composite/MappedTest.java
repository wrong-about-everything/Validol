package Validation.Composite;

import Validation.Result.Result;
import Validation.Value.Present;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.junit.Ignore;
import org.junit.Test;
import Validation.Leaf.Named;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MappedTest
{
    @Test
    public void success() throws Throwable
    {
        Result<List<UnnamedBloc<Item>>> result =
            new Mapped<>(
                this.jsonArrayOfMaps(),
                jsonMapElement ->
                    new UnnamedBloc<>(
                        List.of(
                            new Named<>(
                                "id",
                                Either.right(
                                    new Present<>(jsonMapElement.getAsJsonObject().get("id").getAsInt())
                                )
                            )
                        ),
                        Item.class
                    )
            )
                .result();

        assertTrue(result.isSuccessful());
        assertEquals(Integer.valueOf(1488), result.value().raw().get(0).result().value().raw().id());
    }

    @Test
    public void fail() throws Throwable
    {
        Result<List<UnnamedBloc<Item>>> result =
                new Mapped<>(
                        this.jsonArrayOfMaps(),
                        jsonMapElement ->
                                new UnnamedBloc<>(
                                        List.of(
                                                new Named<>(
                                                        "id",
                                                        Either.left(
                                                                "Wooooooops"
                                                        )
                                                )
                                        ),
                                        Item.class
                                )
                )
                        .result();

        assertTrue(result.isSuccessful());
        Result innerValidatableResult = result.value().raw().get(0).result();
        assertFalse(innerValidatableResult.isSuccessful());
        assertEquals(
                Map.of("id", "Wooooooops"),
                innerValidatableResult.error()

        );
    }

    @Test
    @Ignore
    public void wrongStructure() throws Throwable
    {
    }

    private JsonElement jsonArrayOfMaps()
    {
        HashMap<String, Object> inner1 = new HashMap<>();
        inner1.put("id", 1488);
        List<HashMap<String, Object>> target = List.of(inner1);

        return new Gson().toJsonTree(target, new TypeToken<List<HashMap<String, Object>>>() {}.getType());
    }
}

class Item
{
    private Integer id;

    public Item(Integer id)
    {
        this.id = id;
    }

    public Integer id()
    {
        return this.id;
    }
}
