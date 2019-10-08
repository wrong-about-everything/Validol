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
public class IsUrlTest
{
    @Test
    public void validationFailedWhenDecoratedElementIsInvalid() throws Throwable
    {
        IsUrl named =
            new IsUrl(
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
    @UseDataProvider("invalidUrls")
    public void validationFailedWithInvalidUrl(String url) throws Throwable
    {
        IsUrl named =
            new IsUrl(
                new Named<>(
                    "vasya",
                    Either.right(new Present<>(new JsonPrimitive(url)))
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a valid url.", named.result().error());
    }

    @DataProvider
    public static Object[][] invalidUrls()
    {
        return
            new Object[][] {
                {"Woooops"},
                {"#@%^%#$@#$@#.com"},
                {":abc.com"},
                {"abc/.com"},
                {"abc#a.com"},
                {"abc?.com"},
                {"abc&.com"},
                {"abc@.com"},
                {"abc%+~.com"},
                {"abc+~.com"},
                {"abc~.com"},
            };
    }

    @Test
    @UseDataProvider("validUrls")
    public void validationSucceededWithValidUrl(String url) throws Throwable
    {
        IsUrl named =
            new IsUrl(
                new Named<>(
                    "vasya",
                    Either.right(new Present<>(new JsonPrimitive(url)))
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(url, named.result().value().raw().getAsString());
    }

    @DataProvider
    public static Object[][] validUrls()
    {
        return
            new Object[][] {
                {"https://en.wikipedia.org/wiki/Möbius_strip"},
                {"http://www.example.com/grave`accent"},
                {"https://zh.wikipedia.org/wiki/%E5%8C%97%E4%BA%AC%E5%B8%82"},
            };
    }

    @Test
    public void validationSucceededWithEmptyUrl() throws Throwable
    {
        IsUrl named =
            new IsUrl(
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
        IsUrl named =
            new IsUrl(
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
        assertEquals("This value must be a json primitive.", named.result().error());
    }
}
