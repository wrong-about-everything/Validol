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
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class IsNumberTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
    {
        IsNumber named =
            new IsNumber(
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
        IsNumber named =
            new IsNumber(
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
        assertEquals("This value must be a number.", named.result().error());
    }

    @Test
    public void successfulWithAbsentValue() throws Throwable
    {
        IsNumber named = new IsNumber(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithBoolean() throws Throwable
    {
        IsNumber named =
            new IsNumber(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Present<>(new JsonPrimitive(true))
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a number.", named.result().error());
    }

    @Test
    @UseDataProvider("validNumbers")
    public void successfulWithPresentValue(String number) throws Throwable
    {
        IsNumber named = new IsNumber(new Named<>("vasya", Either.right(new Present<>(new JsonPrimitive(number)))));

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
