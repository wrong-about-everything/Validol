package validation.leaf.is.of.format;

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
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.format.nonblank.IsNotBlank;
import validation.result.value.Absent;
import validation.result.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
final public class IsNotBlankTest
{
    @Test
    public void failedWithInvalidOriginalValidatable() throws Exception
    {
        IsNotBlank named =
            new IsNotBlank(
                new NamedStub<>(
                    "vasya",
                    Either.left(new ErrorStub("Wooops"))
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error().value().get("message"));
    }

    @Test
    public void failedWithIncorrectStructure() throws Exception
    {
        IsNotBlank named =
            new IsNotBlank(
                new NamedStub<>(
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
        assertEquals("This value must be a json primitive.", named.result().error().value().get("message"));
    }

    @Test
    public void successWithNonExistentField() throws Exception
    {
        IsNotBlank named =
            new IsNotBlank(
                new NamedStub<>(
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
    public void successfulWithNonBlankData(JsonElement json) throws Exception
    {
        IsNotBlank named = new IsNotBlank(new NamedStub<>("vasya", Either.right(new Present<>(json))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
    }

    @DataProvider
    public static Object[][] nonBlankData()
    {
        return
            new Object[][] {
                {new JsonPrimitive("Woooops")},
                {new JsonPrimitive('b')},
                {new JsonPrimitive(5)},
                {new JsonPrimitive(3.1416)},
                {new JsonPrimitive(true)},
                {new JsonPrimitive(false)},
            };
    }
}
