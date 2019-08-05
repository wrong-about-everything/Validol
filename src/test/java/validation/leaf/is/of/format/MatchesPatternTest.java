package validation.leaf.is.of.format;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class MatchesPatternTest {
    @Test
    public void validationFailedWhenDecoratedElementIsInvalid() throws Throwable {
        MatchesPattern named =
            new MatchesPattern(
                new Named<>(
                    "vasya",
                    Either.left("Wooops")
                ),
                Pattern.compile(".*")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error());
    }

    @Test
    public void validationFailedWithInvalidStructure() throws Throwable {
        MatchesPattern named =
            new MatchesPattern(
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
                                    "delivery_by", "vasya"
                                ),
                                new TypeToken<HashMap<String, Object>>() {
                                }.getType()
                            )
                        )
                    )
                ),
                Pattern.compile(".*")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be a string.", named.result().error());
    }

    @Test
    public void validationFailedWithNonMatchedPattern() throws Throwable {
        MatchesPattern named =
            new MatchesPattern(
                new Named<>(
                    "vasya",
                    Either.right(new Present<>(new JsonPrimitive("hello")))
                ),
                Pattern.compile("\\d{2}")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must match a pattern \\d{2}", named.result().error());
    }

    @Test
    public void validationSucceededWithAbsentField() throws Throwable {
        MatchesPattern named =
            new MatchesPattern(
                new Named<>(
                    "vasya",
                    Either.right(new Absent<>())
                ),
                Pattern.compile("[a-z0-9]?")
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void validationSucceededWithMatchedPattern() throws Throwable {
        MatchesPattern named =
            new MatchesPattern(
                new Named<>(
                    "vasya",
                    Either.right(new Present<>(new JsonPrimitive("abc")))
                ),
                Pattern.compile(".*")
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("abc", named.result().value().raw());
    }
}
