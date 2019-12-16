package validation.leaf.is.of.format;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.format.lengthisbetween.LengthIsBetween;
import validation.result.value.Absent;
import validation.result.value.Present;
import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class LengthIsBetweenTest
{
    @Test
    public void failedWithInvalidOriginalValidatable() throws Exception
    {
        LengthIsBetween named =
            new LengthIsBetween(
                new NamedStub<>(
                    "vasya",
                    Either.left(new ErrorStub("Wooops"))
                ),
                0,
                1
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error().value().get("message"));
    }

    @Test
    public void successWithNonExistentField() throws Exception
    {
        LengthIsBetween named =
            new LengthIsBetween(
                new NamedStub<>(
                    "vasya",
                    Either.right(
                        new Absent<>()
                    )
                ),
                0,
                1
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    @UseDataProvider("nonMatchingLengths")
    public void failedWithNonMatchingLengths(String value, Integer min, Integer max) throws Exception
    {
        LengthIsBetween named =
            new LengthIsBetween(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(value))
                ),
                min,
                max
            );

        assertFalse(named.result().isSuccessful());
        assertEquals(
            String.format("The length of this value must be between %d and %d, inclusively.", min, max),
            named.result().error().value().get("message")
        );
    }

    @DataProvider
    public static Object[][] nonMatchingLengths()
    {
        return
            new Object[][] {
                {"vasya", 6, 16},
                {"vasya", 1, 4},
            };
    }

    @Test
    @UseDataProvider("matchingLengths")
    public void successfulWithMatchingLengths(String value, Integer min, Integer max) throws Exception
    {
        LengthIsBetween named =
            new LengthIsBetween(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>(value))
                ),
                min,
                max
            );

        assertTrue(named.result().isSuccessful());
        assertEquals(value, named.result().value().raw());
    }

    @DataProvider
    public static Object[][] matchingLengths()
    {
        return
            new Object[][] {
                {"vasya", 1, 5},
                {"vasya", 3, 5},
                {"vasya", 3, 7},
                {"vasya", 5, 7},
            };
    }
}
