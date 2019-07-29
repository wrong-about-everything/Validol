package validation.leaf.is;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class IsNotBlankTest
{
    @Test
    public void failedWithInvalidOriginalValidatable() throws Throwable
    {
        IsBlank named =
            new IsBlank(
                new Named<>(
                    "vasya",
                    Either.left("Wooops")
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error());
    }

    @Test
    public void failedWithIncorrectStructure() throws Throwable
    {
        IsBlank named =
            new IsBlank(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Present<>(
                            new Gson().toJsonTree(
                                List.of(
                                    Map.of("id", 1900),
                                    Map.of("id", 777)
                                ),
                                new TypeToken<List<Map<String, Object>>>() {}.getType()
                            )
                        )
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be blank.", named.result().error());
    }

    @Test
    public void successWithNonExistentField() throws Throwable
    {
        IsBlank named =
            new IsBlank(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Absent<>()
                    )
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    @UseDataProvider("nonBlankData")
    public void failedWithNonBlankData(JsonElement json) throws Throwable
    {
        IsBlank named = new IsBlank(new Named<>("vasya", Either.right(new Present<>(json))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
    }

    @DataProvider
    public static Object[][] nonBlankData()
    {
        return
            new Object[][] {
                {new JsonPrimitive("Woooops")},
                {new JsonPrimitive(777)},
                {new JsonPrimitive('b')},
                {new JsonPrimitive(true)},
            };
    }
}
