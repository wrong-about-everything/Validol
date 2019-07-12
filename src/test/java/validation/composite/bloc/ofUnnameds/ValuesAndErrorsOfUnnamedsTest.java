package validation.composite.bloc.ofUnnameds;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import org.javatuples.Pair;
import org.junit.Test;
import validation.composite.bloc.ofNameds.UnnamedBlocOfNameds;
import validation.composite.bloc.ofUnnameds.dataClass.Integers;
import validation.composite.bloc.ofUnnameds.dataClass.Item;
import validation.composite.bloc.ofUnnameds.dataClass.Items;
import validation.composite.bloc.ofUnnameds.dataClass.Strings;
import validation.leaf.Named;
import validation.leaf.Unnamed;
import validation.leaf.is.IsInteger;
import validation.result.Result;
import validation.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ValuesAndErrorsOfUnnamedsTest
{
    @Test
    public void noErrorsInPlainUnnameds() throws Throwable
    {
        Pair<List<Object>, List<Object>> valuesAndErrors =
            new ValuesAndErrorsOfUnnameds(
                List.of(
                    new Unnamed<>(
                        Either.right(
                            new Present<>("vasya")
                        )
                    ),
                    new Unnamed<>(
                        Either.right(
                            new Present<>(123)
                        )
                    ),
                    new Unnamed<>(
                        Either.right(
                            new Present<>(true)
                        )
                    )
                )
            )
                .value();

        assertEquals(List.of("vasya", 123, true), valuesAndErrors.getValue0());
        assertEquals(List.of(), valuesAndErrors.getValue1());
    }

    @Test
    public void failedValidationOfPlainUnnameds() throws Throwable
    {
        Pair<List<Object>, List<Object>> valuesAndErrors =
            new ValuesAndErrorsOfUnnameds(
                List.of(
                    new Unnamed<>(
                        Either.left(
                            "error"
                        )
                    ),
                    new Unnamed<>(
                        Either.left(
                            "error 2"
                        )
                    )
                )
            )
                .value();

        assertEquals(List.of(), valuesAndErrors.getValue0());
        assertEquals(List.of("error", "error 2"), valuesAndErrors.getValue1());
    }

    @Test
    public void successfulValidationOfUnnamedBlocks() throws Throwable
    {
        Pair<List<Object>, List<Object>> valuesAndErrors =
            new ValuesAndErrorsOfUnnameds(
                List.of(
                    new UnnamedBlocOfUnnameds<>(
                        this.jsonArrayOfIntegers(),
                        (e) ->
                            new IsInteger(
                                new Unnamed<>(
                                    Either.right(
                                        new Present<>(
                                            e.toString()
                                        )
                                    )
                                )
                            ),
                        Integers.class
                    ),
                    new UnnamedBlocOfUnnameds<>(
                        this.jsonArrayOfStrings(),
                        (e) ->
                            new Unnamed<>(
                                Either.right(
                                    new Present<>(
                                        e.toString()
                                    )
                                )
                            ),
                        Strings.class
                    )
                )
            )
                .value();

        assertEquals(
                new Integers(
                    List.of(1488, 666)
                )
            ,
            valuesAndErrors.getValue0().get(0)
        );
        assertEquals(
            new Strings(
                List.of("vasya", "fedya")
            ),
            valuesAndErrors.getValue0().get(1)
        );
        assertEquals(List.of(), valuesAndErrors.getValue1());
    }

    @Test
    public void fail() throws Throwable
    {
        Result<Items> result =
            new UnnamedBlocOfUnnameds<>(
                this.jsonArrayOfMaps(),
                jsonMapElement ->
                    new UnnamedBlocOfNameds<>(
                        List.of(
                            new Named<>(
                                "id",
                                Either.left("Wooooooops")
                            )
                        ),
                        Item.class
                    ),
                Items.class
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals(
            List.of(
                Map.of("id", "Wooooooops"),
                Map.of("id", "Wooooooops")
            ),
            result.error()
        );
    }

    @Test
    public void wrongStructure() throws Throwable
    {
        Result<Items> result =
            new UnnamedBlocOfUnnameds<>(
                this.messyJson(),
                jsonMapElement ->
                    new UnnamedBlocOfNameds<>(
                        List.of(
                            new Named<>(
                                "id",
                                Either.left("Wooooooops")
                            )
                        ),
                        Item.class
                    ),
                Items.class
            )
                .result();

        assertFalse(result.isSuccessful());
        assertEquals(
            "This block must be an array.",
            result.error()
        );
    }

    private JsonElement jsonArrayOfMaps()
    {
        return
            new Gson().toJsonTree(
                List.of(
                    Map.of("id", 1488),
                    Map.of("id", 666)
                ),
                new TypeToken<List<Map<String, Object>>>() {}.getType()
            );
    }

    private JsonElement jsonArrayOfIntegers()
    {
        return
            new Gson().toJsonTree(
                List.of(1488, 666),
                new TypeToken<List<Integer>>() {}.getType()
            );
    }

    private JsonElement jsonArrayOfStrings()
    {
        return
            new Gson().toJsonTree(
                List.of("vasya", "fedya"),
                new TypeToken<List<Integer>>() {}.getType()
            );
    }

    private JsonElement messyJson()
    {
        return
            new Gson().toJsonTree(
                Map.of(
                    "id", 666,
                    "vasya", "belov"
                ),
                new TypeToken<Object>() {}.getType()
            );
    }
}
