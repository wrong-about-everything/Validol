package validation.leaf.as.format;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.result.value.Absent;
import validation.result.value.Present;

import java.net.URL;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
final public class AsUrlTest
{
    @Test
    public void validationFailedWhenDecoratedElementIsInvalid() throws Exception
    {
        AsUrl named =
            new AsUrl(
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
    @UseDataProvider("invalidUrls")
    public void validationFailedWithInvalidUrl(String url) throws Exception
    {
        AsUrl named =
            new AsUrl(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(url))
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a valid url.", named.result().error().value().get("message"));
    }

    @Test
    @UseDataProvider("invalidUrls")
    public void validationFailedWithInvalidUrlAndCustomErrorMessage(String url) throws Exception
    {
        AsUrl named =
            new AsUrl(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(url))
                ),
                new ErrorStub("hey vasya!")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("hey vasya!", named.result().error().value().get("message"));
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
    public void validationSucceededWithValidUrl(String url) throws Exception
    {
        AsUrl named =
            new AsUrl(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(url))
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(new URL(url), named.result().value().raw());
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
    public void validationSucceededWithEmptyField() throws Exception
    {
        AsUrl named =
            new AsUrl(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Absent<>())
                )
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertFalse(named.result().value().isPresent());
    }
}
