package validation.leaf.is.of.format;

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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class IsUuidTest
{
    @Test
    public void validationFailedWhenDecoratedElementIsInvalid() throws Throwable
    {
        IsUuid named =
            new IsUuid(
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
    @UseDataProvider("invalidUuids")
    public void validationFailedWithInvalidUuid(String uuid) throws Throwable
    {
        IsUuid named =
            new IsUuid(
                new Named<>(
                    "vasya",
                    Either.right(new Present<>(new JsonPrimitive(uuid)))
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a valid uuid.", named.result().error());
    }

    @DataProvider
    public static Object[][] invalidUuids()
    {
        return
            new Object[][] {
                {"Woooops"},
                {"#@%^%#$@#$@#.com"},
                {"1116333f-9f87-416e-a18a-48375e9b20"},
                {"1116333f-9f87-416e-a18a-48375e9b204"},
                {"1116333f9f87-416e-a18a-48375e9b2042"},
                {"1116333f9f87-416e-a18a--48375e9b2042"},
                {"1116333f-9f87-416e-a18a-48375e9b204z"},
            };
    }

    @Test
    @UseDataProvider("validUuids")
    public void validationSucceededWithValidUuid(String Uuid) throws Throwable
    {
        IsUuid named =
            new IsUuid(
                new Named<>(
                    "vasya",
                    Either.right(new Present<>(new JsonPrimitive(Uuid)))
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(Uuid, named.result().value().raw().toString());
    }

    @DataProvider
    public static Object[][] validUuids()
    {
        return
            new Object[][] {
                {"https://en.wikipedia.org/wiki/Möbius_strip"},
                {"http://www.example.com/grave`accent"},
                {"https://zh.wikipedia.org/wiki/%E5%8C%97%E4%BA%AC%E5%B8%82"},
            };
    }

    @Test
    public void validationSucceededWithEmptyUuid() throws Throwable
    {
        IsUuid named =
            new IsUuid(
                new Named<>(
                    "vasya",
                    Either.right(new Absent<>())
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void validationFailedWithInvalidStructure() throws Throwable
    {
        IsUuid named =
            new IsUuid(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Present<>(
                            new Gson().toJsonTree(
                                Map.of(
                                    "guest", Map.of(
                                        "Uuid", "samokhinvadim@gmail.com",
                                        "name", "Vadim Samokhin"
                                    ),
                                    "delivery_by","vasya"
                                ),
                                new TypeToken<HashMap<String, Object>>() {}.getType()
                            )
                        )
                    )
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a string.", named.result().error());
    }
}
