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
import validation.leaf.Named;
import validation.leaf.is.of.value.IsFalse;
import validation.value.Absent;
import validation.value.Present;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class LengthIsBetweenTest
{
    @Test
    public void failedWithInvalidOriginalValidatable() throws Throwable
    {
        LengthIsBetween named =
            new LengthIsBetween(
                new Named<>(
                    "vasya",
                    Either.left("Wooops")
                ),
                0,
                1
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error());
    }

    @Test
    public void failedWithIncorrectStructure() throws Throwable
    {
        LengthIsBetween named =
            new LengthIsBetween(
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
                ),
                0,
                1
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a string.", named.result().error());
    }

    @Test
    public void failedWithNonString() throws Throwable
    {
        LengthIsBetween named =
            new LengthIsBetween(
                new Named<>(
                    "vasya",
                    Either.right(
                        new Present<>(new JsonPrimitive(777))
                    )
                ),
                0,
                2
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a string.", named.result().error());
    }

    @Test
    public void successWithNonExistentField() throws Throwable
    {
        LengthIsBetween named =
            new LengthIsBetween(
                new Named<>(
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
    public void failedWithNonMatchingLengths(String value, Integer min, Integer max) throws Throwable
    {
        LengthIsBetween named =
            new LengthIsBetween(
                new Named<>(
                    "vasya",
                    Either.right(new Present<>(new JsonPrimitive(value)))
                ),
                min,
                max
            );

        assertFalse(named.result().isSuccessful());
        assertEquals(
            String.format("The length of this value must be between %d and %d, inclusively.", min, max),
            named.result().error()
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
    public void successfulWithMatchingLengths(String value, Integer min, Integer max) throws Throwable
    {
        LengthIsBetween named =
            new LengthIsBetween(
                new Named<>(
                    "vasya",
                    Either.right(new Present<>(new JsonPrimitive(value)))
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
