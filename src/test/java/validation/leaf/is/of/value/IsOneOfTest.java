package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;

import java.util.List;

import static org.junit.Assert.*;

public class IsOneOfTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
    {
        IsOneOf<?> named =
            new IsOneOf<>(
                new Named<>(
                    "vasya",
                    Either.left("Wooops")
                ),
                List.of(1)
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error());
    }

    @Test
    public void successfulWithAbsentValue() throws Throwable
    {
        IsOneOf<?> named =
            new IsOneOf<>(
                new Named<>("vasya", Either.right(new Absent<>())),
                List.of(25)
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithIntegerValueAbsentInList() throws Throwable
    {
        IsOneOf<?> named =
            new IsOneOf<>(
                new Named<>("vasya", Either.right(new Present<>(777))),
                List.of(888, 999)
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be one of the following: 888, 999.", named.result().error());
    }

    @Test
    public void successfulWithStringValuePresentInList() throws Throwable
    {
        IsOneOf<?> named =
            new IsOneOf<>(
                new Named<>("vasya", Either.right(new Present<>("vasya"))),
                List.of("fedya", "vasya", "vitya")
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("vasya", named.result().value().raw());
    }
}
