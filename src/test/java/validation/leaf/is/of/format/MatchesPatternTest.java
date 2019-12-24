package validation.leaf.is.of.format;

import com.spencerwi.either.Either;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import validation.ErrorStub;
import validation.leaf.is.NamedStub;
import validation.leaf.is.of.format.pattern.MatchesPattern;
import validation.result.value.Absent;
import validation.result.value.Present;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
final public class MatchesPatternTest {
    @Test
    public void validationFailedWhenDecoratedElementIsInvalid() throws Exception {
        MatchesPattern named =
            new MatchesPattern(
                new NamedStub<>(
                    "vasya",
                    Either.left(new ErrorStub("Wooops"))
                ),
                Pattern.compile(".*")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error().value().get("message"));
    }

    @Test
    public void validationFailedWithNonMatchedPattern() throws Exception {
        MatchesPattern named =
            new MatchesPattern(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>("hello"))
                ),
                Pattern.compile("\\d{2}")
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must match a pattern \\d{2}", named.result().error().value().get("message"));
    }

    @Test
    public void validationSucceededWithAbsentField() throws Exception {
        MatchesPattern named =
            new MatchesPattern(
                new NamedStub<>(
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
    public void validationSucceededWithMatchedPattern() throws Exception {
        MatchesPattern named =
            new MatchesPattern(
                new NamedStub<>(
                    "vasya",
                    Either.right(new Present<>("abc"))
                ),
                Pattern.compile(".*")
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("abc", named.result().value().raw());
    }
}
