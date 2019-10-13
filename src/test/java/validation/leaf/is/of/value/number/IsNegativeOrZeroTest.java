package validation.leaf.is.of.value.number;

import com.spencerwi.either.Either;
import org.junit.Test;
import validation.leaf.Named;
import validation.value.Absent;
import validation.value.Present;
import static org.junit.Assert.*;

public class IsNegativeOrZeroTest
{
    @Test
    public void failedWithFailedOriginalValidatable() throws Throwable
    {
        IsNegativeOrZero named =
            new IsNegativeOrZero(
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
    public void successfulWithAbsentValue() throws Throwable
    {
        IsNegativeOrZero named = new IsNegativeOrZero(new Named<>("vasya", Either.right(new Absent<>())));

        assertTrue(named.result().isSuccessful());
        assertFalse(named.result().value().isPresent());
    }

    @Test
    public void failedWithPresentPositiveValue() throws Throwable
    {
        IsNegativeOrZero named = new IsNegativeOrZero(new Named<>("vasya", Either.right(new Present<>(0.05))));

        assertFalse(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals("This value must be negative or zero.", named.result().error());
    }

    @Test
    public void successfulWithPresentZeroValue() throws Throwable
    {
        IsNegativeOrZero named = new IsNegativeOrZero(new Named<>("vasya", Either.right(new Present<>(0))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(0, named.result().value().raw());
    }

    @Test
    public void successfulWithPresentNegativeValue() throws Throwable
    {
        IsNegativeOrZero named = new IsNegativeOrZero(new Named<>("vasya", Either.right(new Present<>(-0.4))));

        assertTrue(named.result().isSuccessful());
        assertEquals("vasya", named.result().name());
        assertEquals(-0.4, named.result().value().raw());
    }
}
