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
public class IsIpTest
{
    @Test
    public void validationFailedWhenDecoratedElementIsInvalid() throws Throwable
    {
        IsIp named =
            new IsIp(
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
    @UseDataProvider("invalidIps")
    public void validationFailedWithInvalidIp(String ip) throws Throwable
    {
        IsIp named =
            new IsIp(
                new Named<>(
                    "vasya",
                    Either.right(new Present<>(new JsonPrimitive(ip)))
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a valid ip.", named.result().error());
    }

    @DataProvider
    public static Object[][] invalidIps()
    {
        return
            new Object[][] {
                {"Woooops"},
                {"1272.1.2.3"},
                {"1.1.2.1272"},
                {"127.1.v.3"},
            };
    }

    @Test
    @UseDataProvider("validIps")
    public void validationSucceededWithValidIp(String ip) throws Throwable
    {
        IsIp named =
            new IsIp(
                new Named<>(
                    "vasya",
                    Either.right(new Present<>(new JsonPrimitive(ip)))
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(ip, named.result().value().raw().getHostAddress());
    }

    @DataProvider
    public static Object[][] validIps()
    {
        return
            new Object[][] {
                {"127.0.0.1"},
                {"192.168.0.1"},
            };
    }

    @Test
    public void validationSucceededWithEmptyIp() throws Throwable
    {
        IsIp named =
            new IsIp(
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
        IsIp named =
            new IsIp(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Present<>(
                            new Gson().toJsonTree(
                                Map.of(
                                    "guest", Map.of(
                                        "email", "samokhinvadim@gmail.com",
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
