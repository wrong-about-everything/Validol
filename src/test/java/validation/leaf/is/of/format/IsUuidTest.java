package validation.leaf.is.of.format;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.format.uuid.IsUuid;
import validation.result.value.Absent;
import validation.result.value.Present;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
final public class IsUuidTest
{
    @Test
    public void validationFailedWhenDecoratedElementIsInvalid() throws Exception
    {
        IsUuid named =
            new IsUuid(
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
    @UseDataProvider("invalidUuids")
    public void validationFailedWithInvalidUuid(String uuid) throws Exception
    {
        IsUuid named =
            new IsUuid(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(uuid))
                )
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a valid UUID.", named.result().error().value().get("message"));
    }

    @Test
    @UseDataProvider("invalidUuids")
    public void validationFailedWithInvalidUuidAndCustomError(String uuid) throws Exception
    {
        IsUuid named =
            new IsUuid(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(uuid))
                ),
                new ErrorStub("hello, vasya!")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("hello, vasya!", named.result().error().value().get("message"));
    }

    @DataProvider
    public static Object[][] invalidUuids()
    {
        return
            new Object[][] {
                {"Woooops"},
                {"#@%^%#$@#$@#.com"},
                {"1-9-4-a"},
                {"1-9-4-a-4"},
                {"11-9f-41-a1-48"},
                {"1116333f-9f87-416e-a18a-48375e9b204"},
                {"1116333f9f87-416e-a18a-48375e9b2042"},
                {"1116333f9f87-416e-a18a--48375e9b2042"},
                {"1116333f-9f87-416e-a18a-48375e9b204z"},
            };
    }

    @Test
    @UseDataProvider("validUuids")
    public void validationSucceededWithValidUuid(String Uuid) throws Exception
    {
        IsUuid named =
            new IsUuid(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(Uuid))
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
                {"332cde59-3a67-4561-a60b-f8d93ae4e79b"},
                {"e25d5d89-99f8-466e-8668-c3ee0b0a6cdd"},
            };
    }

    @Test
    public void validationSucceededWithEmptyUuid() throws Exception
    {
        IsUuid named =
            new IsUuid(
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
