package validation.leaf.as.format;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.leaf.NamedStub;
import validation.result.value.Absent;
import validation.result.value.Present;
import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class AsIpTest
{
    @Test
    public void validationFailedWhenDecoratedElementIsInvalid() throws Exception
    {
        AsIp named =
            new AsIp(
                new NamedStub<>(
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
    public void validationFailedWithInvalidIp(String ip) throws Exception
    {
        AsIp named =
            new AsIp(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(ip))
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
    public void validationSucceededWithValidIp(String ip) throws Exception
    {
        AsIp named =
            new AsIp(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(ip))
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
    public void validationSucceededWithEmptyIp() throws Exception
    {
        AsIp named =
            new AsIp(
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
