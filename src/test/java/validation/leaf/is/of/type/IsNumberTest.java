package validation.leaf.is.of.type;

import com.google.gson.Gson;
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
import validation.leaf.is.of.type.number.IsNumber;
import validation.result.value.Absent;
import validation.result.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
final public class IsNumberTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Exception
    {
        IsNumber named =
            new IsNumber(
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
        IsNumber named =
            new IsNumber(
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
    public void successfulWithAbsentValue() throws Exception
    {
        IsNumber named = new IsNumber(new NamedStub<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithBoolean() throws Exception
    {
        IsNumber named =
            new IsNumber(
                new NamedStub<>(
                    "vasya",
                    Either.right(
                        new Present<>(new JsonPrimitive(true))
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a number.", named.result().error().value().get("message"));
    }

    @Test
    @UseDataProvider("validNumbers")
    public void successfulWithPresentValue(String number) throws Exception
    {
        IsNumber named = new IsNumber(new NamedStub<>("vasya", Either.right(new Present<>(new JsonPrimitive(number)))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(new JsonPrimitive(number), named.result().value().raw());
    }

    @DataProvider
    public static Object[][] validNumbers()
    {
        return
            new Object[][] {
                {"22"},
                {"-23"},
                {"2.2"},
                {"09"},
            };
    }
}
