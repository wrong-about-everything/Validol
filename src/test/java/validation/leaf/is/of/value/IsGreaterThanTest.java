package validation.leaf.is.of.value;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;

import static org.junit.Assert.*;

public class IsGreaterThanTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
    {
        IsGreaterThan<?> named =
            new IsGreaterThan<>(
                new Named<>(
                    "vasya",
                    Either.left("Wooops")
                ),
                1
            );

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("Wooops", named.result().error());
    }

    @Test
    public void successfulWithAbsentValue() throws Throwable
    {
        IsGreaterThan<?> named =
            new IsGreaterThan<>(
                new Named<>("vasya", Either.right(new Absent<>())),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void successfulWithPresentBooleanValue() throws Throwable
    {
        IsGreaterThan<?> named =
            new IsGreaterThan<>(
                new Named<>("vasya", Either.right(new Present<>(true))),
                false
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(true, named.result().value().raw());
    }

    @Test
    public void successfulWithPresentIntegerValue() throws Throwable
    {
        IsGreaterThan<?> named =
            new IsGreaterThan<>(
                new Named<>("vasya", Either.right(new Present<>(888))),
                777
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(888, named.result().value().raw());
    }

    @Test
    public void failedWithPresentStringValue() throws Throwable
    {
        IsGreaterThan<?> named =
            new IsGreaterThan<>(
                new Named<>("vasya", Either.right(new Present<>("vasya"))),
                "fedya"
            );

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("vasya", named.result().value().raw());
    }
}
